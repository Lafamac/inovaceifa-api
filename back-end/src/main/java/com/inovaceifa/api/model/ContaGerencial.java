package com.inovaceifa.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conta_gerencial")
@Getter
@Setter
@NoArgsConstructor
public class ContaGerencial extends ReferenciaBase {
}