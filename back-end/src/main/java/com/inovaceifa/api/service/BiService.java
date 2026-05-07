package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.bi.*;
import com.inovaceifa.api.dto.relatorio.GestaoVistaDTO;
import com.inovaceifa.api.dto.relatorio.GestaoVistaResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BiService {

    private final GestaoVistaService gestaoVistaService;

    public BiComparativoTalhaoResponseDTO comparativoTalhoes() {

        GestaoVistaResponseDTO gestao =
                gestaoVistaService.listar();

        List<BiComparativoTalhaoDTO> itens =
                gestao.getItens()
                        .stream()
                        .map(this::toDTO)
                        .toList();

        BiComparativoTalhaoDTO melhorLucro =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getLucro())
                        ))
                        .orElse(null);

        BiComparativoTalhaoDTO melhorProdutividade =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getProdutividade())
                        ))
                        .orElse(null);

        BiComparativoTalhaoDTO maiorCusto =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getCusto())
                        ))
                        .orElse(null);

        BiComparativoTalhaoDTO piorMargem =
                itens.stream()
                        .min(Comparator.comparing(
                                i -> nvl(i.getMargem())
                        ))
                        .orElse(null);

        return BiComparativoTalhaoResponseDTO.builder()
                .itens(itens)
                .melhorLucro(melhorLucro)
                .melhorProdutividade(melhorProdutividade)
                .maiorCusto(maiorCusto)
                .piorMargem(piorMargem)
                .build();
    }

    /* =========================================================
       🔥 CONVERSÃO
       ========================================================= */

    private BiComparativoTalhaoDTO toDTO(
            GestaoVistaDTO dto
    ) {

        BigDecimal margem = BigDecimal.ZERO;

        if (nvl(dto.getReceita())
                .compareTo(BigDecimal.ZERO) > 0) {

            margem =
                    nvl(dto.getLucro())
                            .divide(
                                    nvl(dto.getReceita()),
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100));
        }

        return BiComparativoTalhaoDTO.builder()

                .safraTalhaoId(
                        dto.getSafraTalhaoId()
                )

                .talhao(
                        dto.getTalhaoNome()
                )

                .area(
                        dto.getArea()
                )

                .producao(
                        dto.getProducao()
                )

                .produtividade(
                        dto.getProdutividade()
                )

                .custo(
                        dto.getCustoTotal()
                )

                .custoPorHectare(
                        dto.getCustoPorHectare()
                )

                .custoPorSaca(
                        dto.getCustoPorSaca()
                )

                .receita(
                        dto.getReceita()
                )

                .lucro(
                        dto.getLucro()
                )

                .margem(
                        margem
                )

                .vendido(
                        dto.getQuantidadeVendida()
                )

                .estoque(
                        dto.getEstoque()
                )

                .precoMedio(
                        dto.getPrecoMedio()
                )

                .build();
    }

    /* =========================================================
       🔥 NULL SAFE
       ========================================================= */

    private BigDecimal nvl(BigDecimal valor) {

        return valor == null
                ? BigDecimal.ZERO
                : valor;
    }
}