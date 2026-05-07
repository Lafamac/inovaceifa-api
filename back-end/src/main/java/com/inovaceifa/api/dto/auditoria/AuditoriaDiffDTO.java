package com.inovaceifa.api.dto.auditoria;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuditoriaDiffDTO {

    private String campo;
    private String antes;
    private String depois;

    private Long usuarioId;
    private String usuarioNome;

    private LocalDateTime dataEvento;
}