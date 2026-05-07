package com.inovaceifa.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ref_st_cultivo")
@Getter
@Setter
@NoArgsConstructor
public class RefStCultivo extends ReferenciaBase {}