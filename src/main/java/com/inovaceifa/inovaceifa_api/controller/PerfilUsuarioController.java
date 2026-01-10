package com.inovaceifa.api.controller;

import com.inovaceifa.api.model.PerfilUsuario;
import com.inovaceifa.api.service.PerfilUsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilUsuarioController {

    private final PerfilUsuarioService service;

    public PerfilUsuarioController(PerfilUsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<PerfilUsuario> listar() {
        return service.listarTodos();
    }
}
