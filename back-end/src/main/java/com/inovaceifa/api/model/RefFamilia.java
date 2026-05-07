package com.inovaceifa.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ref_familia")
@NoArgsConstructor
@Getter
@Setter
public class RefFamilia extends ReferenciaBase {
}