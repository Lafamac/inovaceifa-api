package com.inovaceifa.api.dto.bi;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BiComparativoSafraResponseDTO {

    private List<BiComparativoSafraDTO> itens;

    private BiComparativoSafraDTO melhorSafra;

    private BiComparativoSafraDTO piorSafra;

    private BiComparativoSafraDTO maiorProdutividade;

    private BiComparativoSafraDTO maiorLucro;
}