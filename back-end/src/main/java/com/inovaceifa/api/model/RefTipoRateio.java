package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ref_tipo_rateio")
@Getter
@Setter
public class RefTipoRateio extends ReferenciaBase {

    private String codigo;

    private String descricao;
}