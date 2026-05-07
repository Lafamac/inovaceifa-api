package com.inovaceifa.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ref_operacao_talhao")
@Getter
@Setter
@NoArgsConstructor
public class RefOperacaoTalhao extends ReferenciaBase {
}