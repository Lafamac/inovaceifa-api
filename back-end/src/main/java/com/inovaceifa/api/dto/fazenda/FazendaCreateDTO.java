package com.inovaceifa.api.dto.fazenda;

import com.inovaceifa.api.validation.CNPJValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Dados para cadastro de uma fazenda")
public class FazendaCreateDTO {

    @Schema(
            example = "10",
            description = "ID do proprietário (obrigatório apenas para super usuário)"
    )
    private Long proprietarioId;

    @Schema(example = "Fazenda Santa Luzia")
    @NotBlank(message = "Nome da fazenda é obrigatório")
    @Size(max = 150)
    private String nome;

    @Schema(example = "12.345.678/0001-90")
    @NotBlank(message = "CNPJ é obrigatório")
    @CNPJValido
    private String cnpj;

    @Schema(example = "1", description = "ID da safra ativa (opcional)")
    private Long safraAtivaId;

    @Schema(example = "Rodovia BR 153, Km 50")
    @Size(max = 150)
    private String endereco;

    @Schema(example = "Uberlândia")
    @Size(max = 100)
    private String cidade;

    @Schema(example = "MG")
    @Size(max = 2)
    private String estado;

    @Schema(example = "Safra 2025")
    private String nomeSafraInicial;

}
