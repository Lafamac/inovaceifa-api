package com.inovaceifa.api.service;

import com.inovaceifa.api.model.PerfilUsuario;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario criarUsuario(String nome,
                                String email,
                                String senha,
                                PerfilUsuario perfil) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setEmail(email);

        // 🔐 senha criptografada
        usuario.setSenha(passwordEncoder.encode(senha));

        usuario.setPerfil(perfil);
        usuario.setCriadoEm(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }
}

