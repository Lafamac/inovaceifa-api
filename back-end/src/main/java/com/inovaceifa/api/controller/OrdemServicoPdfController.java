package com.inovaceifa.api.controller;

import com.inovaceifa.api.service.OrdemServicoPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ordem-servico")
@RequiredArgsConstructor
public class OrdemServicoPdfController {

    private final OrdemServicoPdfService service;

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> gerarPdf(@PathVariable Long id) {

        byte[] pdf = service.gerarPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=os.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}