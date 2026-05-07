package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.planejamento.AdubacaoResponseDTO;
import com.inovaceifa.api.service.AdubacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Adubação",
        description = "Controle de adubação"
)
@RestController
@RequestMapping("/planejamento/adubacao")
@RequiredArgsConstructor
public class AdubacaoController {

    private final AdubacaoService service;

    @GetMapping
    public AdubacaoResponseDTO get(@RequestParam Long safraId) {
        return service.gerar(safraId);
    }
}