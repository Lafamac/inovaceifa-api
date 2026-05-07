package com.inovaceifa.api.dto.talhao;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TalhaoResponseDTO {

    private Long id;
    private String nome;
    private Long fazendaId;
    private LocalDateTime dataCriacao;

    private Long resistenciaFerrugemId;
    private Long sistemaCultivoId;

    private BigDecimal area;
    private BigDecimal espacamentoRua;
    private BigDecimal espacamentoPlanta;
    private String material;

    private Boolean ativo;
}