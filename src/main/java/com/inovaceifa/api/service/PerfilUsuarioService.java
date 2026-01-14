package com.inovaceifa.api.service;

import com.inovaceifa.api.model.PerfilUsuario;
import com.inovaceifa.api.repository.PerfilUsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilUsuarioService {

    private final PerfilUsuarioRepository repository;

    public PerfilUsuarioService(PerfilUsuarioRepository repository) {
        this.repository = repository;
    }

    public List<PerfilUsuario> listarTodos() {
        return repository.findAll();
    }

    public PerfilUsuario buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil de usuário não encontrado"));
    }
}


