package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "planejamento_operacao")
public class PlanejamentoOperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* CONTEXTO */

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id")
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id")
    private Fazenda fazenda;

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    /* RELACIONAMENTOS */

    @ManyToOne(optional = false)
    @JoinColumn(name = "safra_talhao_id")
    private SafraTalhao safraTalhao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacao_id")
    private CadastroOperacao operacao;

    /* DADOS */

    private LocalDate dataPrevista;

    @Column(name = "area_planejada", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaPlanejada;

    @Column(precision = 10, scale = 2)
    private BigDecimal velocidade;

    @Column(precision = 10, scale = 2)
    private BigDecimal eficiencia;

    @Column(name = "horas_previstas", precision = 10, scale = 2)
    private BigDecimal horasPrevistas;

    @Column(name = "diesel_previsto", precision = 10, scale = 2)
    private BigDecimal dieselPrevisto;

    @Column(name = "custo_insumos", precision = 15, scale = 2)
    private BigDecimal custoInsumos;

    @Column(name = "custo_maquinas", precision = 15, scale = 2)
    private BigDecimal custoMaquinas;

    @Column(name = "custo_combustivel", precision = 15, scale = 2)
    private BigDecimal custoCombustivel;

    @Column(name = "custo_total", precision = 15, scale = 2)
    private BigDecimal custoTotal;

    private String status;

    private Boolean ativo = true;
}