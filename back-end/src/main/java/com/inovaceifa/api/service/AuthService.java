package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.login.LoginRequestDTO;
import com.inovaceifa.api.dto.login.LoginResponseDTO;
import com.inovaceifa.api.dto.MeResponseDTO;
import com.inovaceifa.api.dto.login.TrocarSenhaDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final FazendaRepository fazendaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ContextoFazendaService contextoFazendaService;

    /* =========================================================
       LOGIN
       ========================================================= */
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new AuthException("Usuário ou senha inválidos");
        }

        Long perfilId = usuario.getPerfilId();

        /* =========================================================
           VALIDAÇÃO DE PROPRIETÁRIO
           ========================================================= */
        if (perfilId != null && perfilId == 1) {

            Proprietario proprietario = proprietarioRepository
                    .findByUsuario_Id(usuario.getId())
                    .orElseThrow(() ->
                            new AuthException("Proprietário não encontrado")
                    );

            if (proprietario.getAtivo() == null || !proprietario.getAtivo()) {
                throw new AuthException("Proprietário inativo. Acesso bloqueado.");
            }
        }

        /* =========================================================
           VALIDAÇÃO DE FUNCIONÁRIO
           ========================================================= */
        if (perfilId != null && perfilId == 3) {

            Funcionario funcionario = funcionarioRepository
                    .findByUsuarioId(usuario.getId())
                    .orElseThrow(() ->
                            new AuthException("Usuário não vinculado a nenhum funcionário")
                    );

            Proprietario proprietario = funcionario.getProprietario();

            if (proprietario.getAtivo() == null || !proprietario.getAtivo()) {
                throw new AuthException("Proprietário inativo. Acesso bloqueado.");
            }
        }

        String token = jwtService.gerarToken(
                usuario.getId(),
                usuario.getEmail()
        );

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilId(usuario.getPerfilId())
                .token(token)
                .build();
    }

    /* =========================================================
       /AUTH/ME
       ========================================================= */
    public MeResponseDTO me() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthException("Usuário não autenticado");
        }

        String email = (String) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        Long perfilId = usuario.getPerfilId();

        /* =========================================================
           FUNCIONÁRIO
           ========================================================= */
        if (perfilId != null && perfilId == 3) {

            Funcionario funcionario = funcionarioRepository
                    .findByUsuarioId(usuario.getId())
                    .orElseThrow(() ->
                            new AuthException("Usuário não vinculado a nenhum funcionário")
                    );

            Fazenda fazenda = funcionario.getFazenda();

            MeResponseDTO.FazendaDTO fazendaDTO =
                    MeResponseDTO.FazendaDTO.builder()
                            .id(fazenda.getId())
                            .nome(fazenda.getNome())
                            .build();

            return MeResponseDTO.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .perfilId(usuario.getPerfilId())
                    .tipo("FUNCIONARIO")
                    .fazendaAtiva(fazendaDTO)
                    .fazendas(List.of(fazendaDTO))
                    .build();
        }

        /* =========================================================
           PROPRIETÁRIO OU SUPER USUÁRIO
           ========================================================= */
        if (perfilId != null && (perfilId == 1 || perfilId == 2)) {

            Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtivaOrNull();

            MeResponseDTO.FazendaDTO fazendaDTO = null;

            if (fazendaAtiva != null) {
                fazendaDTO = MeResponseDTO.FazendaDTO.builder()
                        .id(fazendaAtiva.getId())
                        .nome(fazendaAtiva.getNome())
                        .build();
            }

            List<MeResponseDTO.FazendaDTO> fazendas = List.of();

            // 🔥 CORREÇÃO AQUI (SEM REMOVER NADA)
            if (perfilId == 1 || perfilId == 2) {

                Proprietario proprietario;

                if (perfilId == 1) {
                    proprietario = proprietarioRepository
                            .findByUsuario_Id(usuario.getId())
                            .orElseThrow(() ->
                                    new AuthException("Proprietário não encontrado")
                            );
                } else {
                    // SUPER USUÁRIO USA CONTEXTO
                    proprietario = contextoFazendaService.getProprietario();
                }

                fazendas = fazendaRepository
                        .findByProprietarioId(proprietario.getId())
                        .stream()
                        .map(f -> MeResponseDTO.FazendaDTO.builder()
                                .id(f.getId())
                                .nome(f.getNome())
                                .build())
                        .toList();
            }

            return MeResponseDTO.builder()
                    .id(usuario.getId())
                    .nome(usuario.getNome())
                    .email(usuario.getEmail())
                    .perfilId(usuario.getPerfilId())
                    .tipo(perfilId == 2 ? "SUPER_USUARIO" : "PROPRIETARIO")
                    .fazendaAtiva(fazendaDTO)
                    .fazendas(fazendas)
                    .build();
        }

        throw new AuthException("Perfil de usuário inválido");
    }

    /* =========================================================
       TROCAR SENHA
       ========================================================= */
    public void trocarSenha(TrocarSenhaDTO dto) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthException("Usuário não autenticado");
        }

        String email = (String) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenhaAtual(), usuario.getSenha())) {
            throw new AuthException("Senha atual incorreta");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuarioRepository.save(usuario);
    }
}