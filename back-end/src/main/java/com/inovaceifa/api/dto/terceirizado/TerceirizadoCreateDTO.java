package com.inovaceifa.api.dto.terceirizado;

import com.inovaceifa.api.validation.CPFValido;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TerceirizadoCreateDTO {

    @NotBlank
    private String nome;

    @NotBlank
    @CPFValido
    private String cpf;

    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;

    @Email
    private String email;

    private String celular;
    private String imagem;
    private String cargo;
    private BigDecimal salario;
}