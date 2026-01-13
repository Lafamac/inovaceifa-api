package com.inovaceifa.api.controller;

import com.inovaceifa.api.model.PerfilUsuario;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.service.UsuarioService;
import com.inovaceifa.api.dto.UsuarioRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario criarUsuario(@RequestBody UsuarioRequest request) {

        PerfilUsuario perfil = new PerfilUsuario();
        perfil.setId(request.getPerfilId());

        return usuarioService.criarUsuario(
                request.getNome(),
                request.getEmail(),
                request.getSenha(),
                perfil
        );
    }
}
