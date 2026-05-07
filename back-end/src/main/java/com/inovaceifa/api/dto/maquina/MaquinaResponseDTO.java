package com.inovaceifa.api.dto.maquina;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
public class MaquinaResponseDTO {

    private Long id;
    private String nome;
    private String marca;
    private String modelo;
    private String descricao;
    private Long anoFabricacao;
    private String imagem;
    private BigDecimal horimetro;
    private Long tipoPosseId;
    private String tipoPosseDescricao;

    private BigDecimal valorDiaria;
    private LocalDate inicioLocacao;
    private LocalDate fimLocacao;
    private Long diasContratados;
    private BigDecimal valorTotalLocacao;
    private Boolean ativo; // NOVO

    private Long fazendaId;

    private Long tipoMaquinaId;
    private String tipoMaquinaDescricao;
}