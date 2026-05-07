package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fazendas")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fazenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Muitas fazendas pertencem a um proprietário
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, columnDefinition = "char(14)", unique = true)
    private String cnpj;

    @Column(length = 255)
    private String endereco;

    @Column(length = 200)
    private String cidade;

    @Column(columnDefinition = "char(2)")
    private String estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "safra_ativa_id")
    private Safra safraAtiva;

    @Column(nullable = false)
    private Boolean ativo = true;
}