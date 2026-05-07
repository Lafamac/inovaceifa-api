package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.relatorio.*;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GestaoVistaService {

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final SafraTalhaoRepository safraTalhaoRepository;
    private final AdministrativoRepository administrativoRepository;
    private final VendaProducaoRepository vendaRepository;
    private final ContextoFazendaService contexto;

    public GestaoVistaResponseDTO listar() {

        Fazenda f = contexto.getFazendaAtiva();
        Safra s = contexto.getSafraAtiva();

        List<SafraTalhao> talhoes = safraTalhaoRepository.findAll();
        List<OperacaoTalhao> operacoes = operacaoTalhaoRepository.findAll();

        List<GestaoVistaDTO> lista = new ArrayList<>();

        BigDecimal areaTotal = BigDecimal.ZERO;
        BigDecimal producaoTotal = BigDecimal.ZERO;
        BigDecimal custoTotalFazenda = BigDecimal.ZERO;

        for (SafraTalhao st : talhoes) {
            if (isSafra(st, s)) {
                areaTotal = areaTotal.add(nvl(st.getAreaUtilizada()));
                producaoTotal = producaoTotal.add(nvl(st.getProducaoReal()));
            }
        }

        for (OperacaoTalhao op : operacoes) {
            if (isSafra(op, s)) {
                custoTotalFazenda = custoTotalFazenda.add(nvl(op.getCustoTotal()));
            }
        }

        List<Administrativo> administrativos =
                administrativoRepository.findByFazenda_IdAndSafra_Id(f.getId(), s.getId());

        for (SafraTalhao st : talhoes) {

            if (!isSafra(st, s)) continue;

            BigDecimal custoTotal = BigDecimal.ZERO;
            BigDecimal custoInsumos = BigDecimal.ZERO;
            BigDecimal custoCombustivel = BigDecimal.ZERO;
            BigDecimal custoMaoObra = BigDecimal.ZERO;
            BigDecimal custoTerceiros = BigDecimal.ZERO;
            BigDecimal custoMaquinas = BigDecimal.ZERO;

            for (OperacaoTalhao op : operacoes) {

                if (!isMesmoTalhao(op, st)) continue;

                custoTotal = custoTotal.add(nvl(op.getCustoTotal()));
                custoInsumos = custoInsumos.add(nvl(op.getCustoInsumos()));
                custoCombustivel = custoCombustivel.add(nvl(op.getCustoCombustivel()));
                custoMaoObra = custoMaoObra.add(nvl(op.getCustoMaoObra()));
                custoTerceiros = custoTerceiros.add(nvl(op.getCustoTerceiros()));
                custoMaquinas = custoMaquinas.add(nvl(op.getCustoMaquina()));
            }

            BigDecimal custoAdminTalhao = BigDecimal.ZERO;

            for (Administrativo adm : administrativos) {

                BigDecimal valor = nvl(adm.getValorTotalRealizado());
                if (valor.compareTo(BigDecimal.ZERO) == 0) continue;

                String tipo = adm.getTipoRateio() != null
                        ? adm.getTipoRateio().getDescricao()
                        : "AREA";

                BigDecimal rateado = BigDecimal.ZERO;

                switch (tipo) {

                    case "AREA":
                        if (areaTotal.compareTo(BigDecimal.ZERO) > 0) {
                            rateado = valor
                                    .divide(areaTotal, 6, RoundingMode.HALF_UP)
                                    .multiply(nvl(st.getAreaUtilizada()));
                        }
                        break;

                    case "PRODUCAO":
                        if (producaoTotal.compareTo(BigDecimal.ZERO) > 0) {
                            rateado = valor
                                    .divide(producaoTotal, 6, RoundingMode.HALF_UP)
                                    .multiply(nvl(st.getProducaoReal()));
                        }
                        break;

                    case "CUSTO":
                        if (custoTotalFazenda.compareTo(BigDecimal.ZERO) > 0) {
                            rateado = valor
                                    .divide(custoTotalFazenda, 6, RoundingMode.HALF_UP)
                                    .multiply(custoTotal);
                        }
                        break;
                }

                custoAdminTalhao = custoAdminTalhao.add(rateado);
            }

            custoTotal = custoTotal.add(custoAdminTalhao);

            BigDecimal area = nvl(st.getAreaUtilizada());
            BigDecimal producao = nvl(st.getProducaoReal());

            BigDecimal vendido = vendaRepository.sumQuantidadeBySafraTalhao(st.getId());
            BigDecimal receita = vendaRepository.sumValorBySafraTalhao(st.getId());

            BigDecimal estoque = producao.subtract(vendido);

            BigDecimal precoMedio = vendido.compareTo(BigDecimal.ZERO) > 0
                    ? receita.divide(vendido, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal produtividade = area.compareTo(BigDecimal.ZERO) > 0
                    ? producao.divide(area, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal custoHa = area.compareTo(BigDecimal.ZERO) > 0
                    ? custoTotal.divide(area, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal custoSc = producao.compareTo(BigDecimal.ZERO) > 0
                    ? custoTotal.divide(producao, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            BigDecimal lucro = receita.subtract(custoTotal);

            lista.add(
                    GestaoVistaDTO.builder()
                            .safraTalhaoId(st.getId())
                            .talhaoNome(st.getTalhao().getNome())
                            .area(area)
                            .producao(producao)
                            .quantidadeVendida(vendido)
                            .estoque(estoque)
                            .precoMedio(precoMedio)
                            .produtividade(produtividade)
                            .custoTotal(custoTotal)
                            .custoPorHectare(custoHa)
                            .custoPorSaca(custoSc)
                            .receita(receita)
                            .lucro(lucro)
                            .custoInsumos(custoInsumos)
                            .custoCombustivel(custoCombustivel)
                            .custoMaoObra(custoMaoObra)
                            .custoTerceiros(custoTerceiros)
                            .custoMaquinas(custoMaquinas)
                            .build()
            );
        }

        BigDecimal totalArea = BigDecimal.ZERO;
        BigDecimal totalProducao = BigDecimal.ZERO;
        BigDecimal totalVendido = BigDecimal.ZERO;
        BigDecimal totalEstoque = BigDecimal.ZERO;
        BigDecimal totalCusto = BigDecimal.ZERO;
        BigDecimal totalReceita = BigDecimal.ZERO;
        BigDecimal totalLucro = BigDecimal.ZERO;

        GestaoVistaDTO melhor = null;
        GestaoVistaDTO pior = null;

        for (GestaoVistaDTO item : lista) {

            totalArea = totalArea.add(nvl(item.getArea()));
            totalProducao = totalProducao.add(nvl(item.getProducao()));
            totalVendido = totalVendido.add(nvl(item.getQuantidadeVendida()));
            totalEstoque = totalEstoque.add(nvl(item.getEstoque()));
            totalCusto = totalCusto.add(nvl(item.getCustoTotal()));
            totalReceita = totalReceita.add(nvl(item.getReceita()));
            totalLucro = totalLucro.add(nvl(item.getLucro()));

            if (melhor == null || item.getLucro().compareTo(melhor.getLucro()) > 0)
                melhor = item;

            if (pior == null || item.getLucro().compareTo(pior.getLucro()) < 0)
                pior = item;
        }

        BigDecimal custoHa = totalArea.compareTo(BigDecimal.ZERO) > 0
                ? totalCusto.divide(totalArea, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal custoSc = totalProducao.compareTo(BigDecimal.ZERO) > 0
                ? totalCusto.divide(totalProducao, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal produtividade = totalArea.compareTo(BigDecimal.ZERO) > 0
                ? totalProducao.divide(totalArea, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return GestaoVistaResponseDTO.builder()
                .itens(lista)
                .totalArea(totalArea)
                .totalProducao(totalProducao)
                .totalVendido(totalVendido)
                .totalEstoque(totalEstoque)
                .totalCusto(totalCusto)
                .totalReceita(totalReceita)
                .totalLucro(totalLucro)
                .custoPorHectare(custoHa)
                .custoPorSaca(custoSc)
                .produtividadeMedia(produtividade)
                .melhorTalhao(melhor)
                .piorTalhao(pior)
                .build();
    }

    private boolean isSafra(SafraTalhao st, Safra s) {
        return st.getSafra() != null && st.getSafra().getId().equals(s.getId());
    }

    private boolean isSafra(OperacaoTalhao op, Safra s) {
        return op.getSafra() != null && op.getSafra().getId().equals(s.getId());
    }

    private boolean isMesmoTalhao(OperacaoTalhao op, SafraTalhao st) {
        return op.getSafraTalhao() != null &&
                op.getSafraTalhao().getId().equals(st.getId());
    }

    private BigDecimal nvl(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}