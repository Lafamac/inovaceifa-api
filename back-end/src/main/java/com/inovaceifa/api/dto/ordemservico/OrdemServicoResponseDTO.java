package com.inovaceifa.api.dto.ordemservico;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrdemServicoResponseDTO {

    private Long id;

    private String nrOs;

    private Long version;

    private Long operacaoId;
    private String operacaoNome;

    private Long planejamentoOperacaoId;

    private LocalDate dataInicio;
    private LocalDate dataFim;

    private String status;

    private String observacao;

    private BigDecimal custoTotal;

    private Long proprietarioId;
    private Long fazendaId;
    private Long safraId;

    // 🔥 NOVO (sem alterar nada existente)
    private List<TalhaoDTO> talhoes;

    // 🔥 NOVO DTO INTERNO
    @Data
    public static class TalhaoDTO {
        private Long id;
        private String nome;

        public TalhaoDTO(Long id, String nome) {
            this.id = id;
            this.nome = nome;
        }
    }
}