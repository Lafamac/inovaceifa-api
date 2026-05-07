package com.inovaceifa.api.dto.funcionario;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class FuncionarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private Long fazendaId;
    private Long proprietarioId;
    private Boolean possuiUsuario;
    private Boolean ativo;

    /* =========================
       NOVOS CAMPOS
       ========================= */

    private String cargo;
    private BigDecimal salario;
    private LocalDate dtAdmissao;
}