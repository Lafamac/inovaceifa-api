package com.inovaceifa.api.controller;

import com.inovaceifa.api.service.ContaGerencialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Tag(
        name = "Conta Gerencial",
        description = "Tabela de Referencai Conta Gerencial"
)
@RestController
@RequestMapping("/conta-gerencial")
public class ContaGerencialController extends ReferenciaBaseController {

    public ContaGerencialController(ContaGerencialService service) {
        super(service);
    }
}