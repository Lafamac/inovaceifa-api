package com.inovaceifa.api.dto.proprietario;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProprietarioResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private Boolean ativo;
}
