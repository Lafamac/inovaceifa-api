package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ref_tipo_gasto_maquina")
@Getter
@Setter
@NoArgsConstructor
public class RefTipoGastoMaquina extends ReferenciaBase {}