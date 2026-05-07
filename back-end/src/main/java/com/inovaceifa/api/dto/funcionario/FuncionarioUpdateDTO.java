package com.inovaceifa.api.dto.funcionario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class FuncionarioUpdateDTO {

    @Size(max = 100)
    private String nome;

    @Size(max = 150)
    private String endereco;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Size(max = 2)
    private String estado;

    @Email
    @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String celular;

    /* =========================
       NOVOS CAMPOS
       ========================= */

    @Size(max = 50)
    private String cargo;

    private BigDecimal salario;

    private LocalDate dtAdmissao;
}