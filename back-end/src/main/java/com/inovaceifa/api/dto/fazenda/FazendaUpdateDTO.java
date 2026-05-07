package com.inovaceifa.api.dto.fazenda;

import com.inovaceifa.api.validation.CNPJValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Dados para atualização de uma fazenda")
public class FazendaUpdateDTO {

    @Schema(example = "Fazenda Santa Luzia")
    @Size(max = 150)
    private String nome;

    @Schema(example = "Rodovia BR 153, Km 50")
    @Size(max = 150)
    private String endereco;

    @Schema(example = "Uberlândia")
    @Size(max = 100)
    private String cidade;

    @Schema(example = "MG")
    @Size(max = 2)
    private String estado;

    @Schema(
            example = "5",
            description = "ID da safra ativa da fazenda"
    )
    private Long safraAtivaId;
}
