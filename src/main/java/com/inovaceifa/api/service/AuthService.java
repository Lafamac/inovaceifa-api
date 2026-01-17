package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.dto.MeResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.UsuarioRepository;
import com.inovaceifa.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * LOGIN
     */
    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new AuthException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new AuthException("Usuário ou senha inválidos");
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

    /**
     * /ME
     * Retorna o usuário autenticado a partir do JWT
     */
    public MeResponseDTO me() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = (String) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return MeResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilId(usuario.getPerfilId())
                .build();
    }
}
