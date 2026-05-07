package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "proprietario_fazenda_ativa",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "proprietario_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ProprietarioFazendaAtiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;
}
