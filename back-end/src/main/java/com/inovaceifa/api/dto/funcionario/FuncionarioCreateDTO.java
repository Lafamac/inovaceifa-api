package com.inovaceifa.api.dto.funcionario;

import com.inovaceifa.api.validation.CPFValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Schema(description = "Cadastro de funcionário")
public class FuncionarioCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @CPFValido
    private String cpf;

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