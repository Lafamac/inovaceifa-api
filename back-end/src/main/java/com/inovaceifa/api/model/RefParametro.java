package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ref_parametro")
@Getter
@Setter
@NoArgsConstructor
public class RefParametro extends ReferenciaBase {

    private String chave;

    private BigDecimal valor;
}