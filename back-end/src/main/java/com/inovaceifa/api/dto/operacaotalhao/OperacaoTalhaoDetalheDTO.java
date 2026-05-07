package com.inovaceifa.api.dto.operacaotalhao;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class OperacaoTalhaoDetalheDTO {

    private Long id;
    private String talhaoNome;
    private BigDecimal area;

    private BigDecimal custoInsumos;
    private BigDecimal custoCombustivel;
    private BigDecimal custoMaquinas;
    private BigDecimal custoFuncionarios;
    private BigDecimal custoTotal;

    private List<ItemDTO> produtos;
    private List<ItemDTO> combustivel;
    private List<ItemDTO> maquinas;
    private List<ItemDTO> funcionarios;

    @Data
    @Builder
    public static class ItemDTO {
        private String nome;
        private BigDecimal quantidade;
        private BigDecimal total;
    }
}