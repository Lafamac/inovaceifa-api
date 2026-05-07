package com.inovaceifa.api.dto.auditoria;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditoriaOrdemServicoResponseDTO {

    private Long id;
    private Long ordemServicoId;
    private Long usuarioId;

    private String acao;

    private String dadosAntes;
    private String dadosDepois;

    private LocalDateTime dataEvento;
}