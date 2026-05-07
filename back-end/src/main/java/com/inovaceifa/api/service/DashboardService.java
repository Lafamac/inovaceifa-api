package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.dashboard.*;
import com.inovaceifa.api.dto.relatorio.GestaoVistaDTO;
import com.inovaceifa.api.dto.relatorio.GestaoVistaResponseDTO;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OperacaoProdutoRepository produtoRepository;
    private final OperacaoCombustivelRepository combustivelRepository;
    private final HoraMaquinaRepository maquinaRepository;
    private final PedidoCompraRepository compraRepository;

    private final GestaoVistaService gestaoVistaService;

    public DashboardSafraResponseDTO gerar(Long safraId) {

        /* =========================================================
           🔥 GESTÃO À VISTA (FONTE OFICIAL)
           ========================================================= */

        GestaoVistaResponseDTO gestao =
                gestaoVistaService.listar();

        /* =========================================================
           🔥 CUSTOS
           ========================================================= */

        BigDecimal insumos =
                nvl(produtoRepository.sumBySafra(safraId));

        BigDecimal combustivel =
                nvl(combustivelRepository.sumBySafra(safraId));

        BigDecimal maquinas =
                nvl(maquinaRepository.sumBySafra(safraId));

        BigDecimal compras =
                nvl(compraRepository.sumBySafra(safraId));

        /* =========================================================
           🔥 TALHÕES
           ========================================================= */

        List<DashboardTalhaoDTO> porTalhao =
                gestao.getItens()
                        .stream()
                        .map(this::toTalhaoDTO)
                        .collect(Collectors.toList());

        /* =========================================================
           🔥 PREÇO MÉDIO
           ========================================================= */

        BigDecimal precoMedio = BigDecimal.ZERO;

        if (nvl(gestao.getTotalVendido())
                .compareTo(BigDecimal.ZERO) > 0) {

            precoMedio =
                    nvl(gestao.getTotalReceita())
                            .divide(
                                    nvl(gestao.getTotalVendido()),
                                    2,
                                    RoundingMode.HALF_UP
                            );
        }

        /* =========================================================
           🔥 MARGEM
           ========================================================= */

        BigDecimal margem = BigDecimal.ZERO;

        if (nvl(gestao.getTotalReceita())
                .compareTo(BigDecimal.ZERO) > 0) {

            margem =
                    nvl(gestao.getTotalLucro())
                            .divide(
                                    nvl(gestao.getTotalReceita()),
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100));
        }

        /* =========================================================
           🔥 COMERCIALIZAÇÃO
           ========================================================= */

        BigDecimal percentualComercializado =
                BigDecimal.ZERO;

        if (nvl(gestao.getTotalProducao())
                .compareTo(BigDecimal.ZERO) > 0) {

            percentualComercializado =
                    nvl(gestao.getTotalVendido())
                            .divide(
                                    nvl(gestao.getTotalProducao()),
                                    4,
                                    RoundingMode.HALF_UP
                            )
                            .multiply(BigDecimal.valueOf(100));
        }

        /* =========================================================
           🔥 RESPONSE
           ========================================================= */

        return DashboardSafraResponseDTO.builder()

                .custoTotal(
                        nvl(gestao.getTotalCusto())
                )

                .custoPorHectare(
                        nvl(gestao.getCustoPorHectare())
                )

                .custoPorSaca(
                        nvl(gestao.getCustoPorSaca())
                )

                .receitaTotal(
                        nvl(gestao.getTotalReceita())
                )

                .lucroTotal(
                        nvl(gestao.getTotalLucro())
                )

                .margemLucro(
                        margem
                )

                .areaTotal(
                        nvl(gestao.getTotalArea())
                )

                .producaoTotal(
                        nvl(gestao.getTotalProducao())
                )

                .totalVendido(
                        nvl(gestao.getTotalVendido())
                )

                .estoqueTotal(
                        nvl(gestao.getTotalEstoque())
                )

                .precoMedio(
                        precoMedio
                )

                .percentualComercializado(
                        percentualComercializado
                )

                .custoInsumos(
                        insumos
                )

                .custoMaquinas(
                        maquinas.add(combustivel)
                )

                .custoCompras(
                        compras
                )

                .porTalhao(
                        porTalhao
                )

                .build();
    }

    /* =========================================================
       🔥 TALHÃO DTO
       ========================================================= */

    private DashboardTalhaoDTO toTalhaoDTO(
            GestaoVistaDTO dto
    ) {

        return DashboardTalhaoDTO.builder()
                .talhao(dto.getTalhaoNome())
                .custo(dto.getCustoTotal())
                .receita(dto.getReceita())
                .lucro(dto.getLucro())

                .vendido(dto.getQuantidadeVendida())
                .estoque(dto.getEstoque())
                .precoMedio(dto.getPrecoMedio())
                .produtividade(dto.getProdutividade())

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