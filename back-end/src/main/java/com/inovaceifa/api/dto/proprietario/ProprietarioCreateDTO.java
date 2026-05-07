package com.inovaceifa.api.dto.proprietario;

import com.inovaceifa.api.validation.CPFValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Cadastro de proprietário")
public class ProprietarioCreateDTO {

    @NotBlank @Size(max = 100)
    private String nome;

    @NotBlank @CPFValido
    private String cpf;

    @NotBlank @Email @Size(max = 150)
    private String email;

    @Size(max = 20)
    private String celular;

    @Size(max = 150)
    private String endereco;

    @Size(max = 100)
    private String bairro;

    @Size(max = 100)
    private String cidade;

    @Size(max = 2)
    private String estado;
}
