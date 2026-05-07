package com.inovaceifa.api.dto.bi;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BiComparativoTalhaoResponseDTO {

    private List<BiComparativoTalhaoDTO> itens;

    private BiComparativoTalhaoDTO melhorLucro;

    private BiComparativoTalhaoDTO melhorProdutividade;

    private BiComparativoTalhaoDTO maiorCusto;

    private BiComparativoTalhaoDTO piorMargem;
}