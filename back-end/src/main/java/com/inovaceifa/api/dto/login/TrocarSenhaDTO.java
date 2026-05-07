package com.inovaceifa.api.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrocarSenhaDTO {

    @JsonProperty("senhaAtual")
    @NotBlank(message = "Senha atual é obrigatória")
    private String senhaAtual;

    @JsonProperty("novaSenha")
    @NotBlank(message = "Nova senha é obrigatória")
    private String novaSenha;
}
