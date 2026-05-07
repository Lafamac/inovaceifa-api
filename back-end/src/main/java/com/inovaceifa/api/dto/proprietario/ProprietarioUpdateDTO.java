package com.inovaceifa.api.dto.proprietario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProprietarioUpdateDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    private String celular;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;

    private Boolean ativo; // somente super poderá alterar
}
