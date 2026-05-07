package com.inovaceifa.api.dto.operacaocombustivel;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OperacaoCombustivelResponseDTO {

    private Long id;

    private Long operacaoTalhaoId;

    private Long maquinaId;
    private String maquinaNome;

    private BigDecimal litros;

    // 🔥 ADICIONADO
    private BigDecimal valorUnitario;
}