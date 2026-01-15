package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.exception.BadRequestException;
import com.inovaceifa.api.exception.NotFoundException;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenha())) {
            throw new BadRequestException("Senha inválida");
        }

        return LoginResponseDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfilId(usuario.getPerfilId())
                .build();
    }
}
