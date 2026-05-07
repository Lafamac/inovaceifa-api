package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "gastos_maquina")
@Getter
@Setter
@NoArgsConstructor
public class GastoMaquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* =========================================================
       RELACIONAMENTOS
       ========================================================= */

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_gasto_id", nullable = false)
    private RefTipoGastoMaquina tipoGasto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "maquina_id", nullable = false)
    private Maquina maquina;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    /* =========================================================
       DADOS DO GASTO
       ========================================================= */

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 255)
    private String descricao;

    @Column(name = "vlr_usado", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;
}
