package com.inovaceifa.api.dto.terceirizado;

import jakarta.validation.constraints.Email;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TerceirizadoUpdateDTO {

    private String nome;
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