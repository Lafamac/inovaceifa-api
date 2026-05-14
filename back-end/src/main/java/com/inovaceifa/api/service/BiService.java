package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.bi.*;
import com.inovaceifa.api.dto.dashboard.DashboardSafraResponseDTO;
import com.inovaceifa.api.dto.relatorio.GestaoVistaDTO;
import com.inovaceifa.api.dto.relatorio.GestaoVistaResponseDTO;
import com.inovaceifa.api.model.Safra;
import com.inovaceifa.api.repository.SafraRepository;
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
    private final DashboardService dashboardService;
    private final SafraRepository safraRepository;

    /* =========================================================
       🔥 COMPARATIVO TALHÕES
       ========================================================= */

    public BiComparativoTalhaoResponseDTO comparativoTalhoes() {

        GestaoVistaResponseDTO gestao =
                gestaoVistaService.listar();

        List<BiComparativoTalhaoDTO> itens =
                gestao.getItens()
                        .stream()
                        .map(this::toTalhaoDTO)
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
       🔥 COMPARATIVO SAFRAS
       ========================================================= */

    public BiComparativoSafraResponseDTO comparativoSafras() {

        List<Safra> safras =
                safraRepository.findAll();

        List<BiComparativoSafraDTO> itens =
                safras.stream()
                        .map(this::toSafraDTO)
                        .toList();

        BiComparativoSafraDTO melhorSafra =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getMargem())
                        ))
                        .orElse(null);

        BiComparativoSafraDTO piorSafra =
                itens.stream()
                        .min(Comparator.comparing(
                                i -> nvl(i.getMargem())
                        ))
                        .orElse(null);

        BiComparativoSafraDTO maiorProdutividade =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getProdutividade())
                        ))
                        .orElse(null);

        BiComparativoSafraDTO maiorLucro =
                itens.stream()
                        .max(Comparator.comparing(
                                i -> nvl(i.getLucro())
                        ))
                        .orElse(null);

        return BiComparativoSafraResponseDTO.builder()
                .itens(itens)
                .melhorSafra(melhorSafra)
                .piorSafra(piorSafra)
                .maiorProdutividade(maiorProdutividade)
                .maiorLucro(maiorLucro)
                .build();
    }

    /* =========================================================
       🔥 TALHÃO DTO
       ========================================================= */

    private BiComparativoTalhaoDTO toTalhaoDTO(
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
       🔥 SAFRA DTO
       ========================================================= */

    private BiComparativoSafraDTO toSafraDTO(
            Safra safra
    ) {

        DashboardSafraResponseDTO dash =
                dashboardService.gerar(safra.getId());

        return BiComparativoSafraDTO.builder()

                .safraId(
                        safra.getId()
                )

                .safra(
                        safra.getNome()
                )

                .receita(
                        dash.getReceitaTotal()
                )

                .custo(
                        dash.getCustoTotal()
                )

                .lucro(
                        dash.getLucroTotal()
                )

                .margem(
                        dash.getMargemLucro()
                )

                .area(
                        dash.getAreaTotal()
                )

                .producao(
                        dash.getProducaoTotal()
                )

                .produtividade(
                        calcularProdutividade(
                                dash.getProducaoTotal(),
                                dash.getAreaTotal()
                        )
                )

                .vendido(
                        dash.getTotalVendido()
                )

                .estoque(
                        dash.getEstoqueTotal()
                )

                .precoMedio(
                        dash.getPrecoMedio()
                )

                .percentualComercializado(
                        dash.getPercentualComercializado()
                )

                .custoPorHectare(
                        dash.getCustoPorHectare()
                )

                .custoPorSaca(
                        dash.getCustoPorSaca()
                )

                .build();
    }

    /* =========================================================
       🔥 PRODUTIVIDADE
       ========================================================= */

    private BigDecimal calcularProdutividade(
            BigDecimal producao,
            BigDecimal area
    ) {

        if (nvl(area).compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return nvl(producao)
                .divide(
                        nvl(area),
                        2,
                        RoundingMode.HALF_UP
                );
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