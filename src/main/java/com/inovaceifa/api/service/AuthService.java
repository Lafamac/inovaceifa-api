package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.exception.NotFoundException;
import com.inovaceifa.api.exception.UnauthorizedException;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.UsuarioRepository;
import com.inovaceifa.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new UnauthorizedException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario.getId(), usuario.getEmail());

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilId(usuario.getPerfilId())
                .token(token)
                .build();
    }
}
