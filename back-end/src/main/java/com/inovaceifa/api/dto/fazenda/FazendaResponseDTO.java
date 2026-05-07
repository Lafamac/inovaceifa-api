package com.inovaceifa.api.dto.fazenda;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Dados de retorno da fazenda")
public class FazendaResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String endereco;
    private String cidade;
    private String estado;

    @Schema(description = "ID da safra ativa")
    private Long safraAtivaId;
}
