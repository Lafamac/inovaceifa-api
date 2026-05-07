package com.inovaceifa.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ref_tipo_pagamento")
@Getter
@Setter
public class RefTipoPagamento extends ReferenciaBase {
}