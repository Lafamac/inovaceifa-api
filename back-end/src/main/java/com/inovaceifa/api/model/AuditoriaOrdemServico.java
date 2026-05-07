package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_ordem_servico")
@Getter
@Setter
@NoArgsConstructor
public class AuditoriaOrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ordem_servico_id")
    private Long ordemServicoId;

    @Column(name = "usuario_id")
    private Long usuarioId;

    private String acao;

    @Column(name = "dados_antes", columnDefinition = "TEXT")
    private String dadosAntes;

    @Column(name = "dados_depois", columnDefinition = "TEXT")
    private String dadosDepois;

    @Column(name = "data_evento")
    private LocalDateTime dataEvento;
}