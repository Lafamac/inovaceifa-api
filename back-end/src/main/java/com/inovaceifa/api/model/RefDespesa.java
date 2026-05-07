package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tab_ref_despesa")
@Getter
@Setter
@NoArgsConstructor
public class RefDespesa extends ReferenciaBase {}