package com.inovaceifa.api.dto.terceirizado;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TerceirizadoResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private Long fazendaId;
    private Long proprietarioId;
    private String cargo;
    private BigDecimal salario;
    private Boolean ativo;
}