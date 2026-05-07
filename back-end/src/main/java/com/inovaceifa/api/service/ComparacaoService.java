package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.relatorio.ComparacaoCompletaResponseDTO;
import com.inovaceifa.api.dto.relatorio.ComparacaoTalhaoResponseDTO;
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
public class ComparacaoService {

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final SafraTalhaoRepository safraTalhaoRepository;
    private final AdministrativoRepository administrativoRepository;
    private final ContextoFazendaService contexto;

    public ComparacaoCompletaResponseDTO compararCompleto() {

        Fazenda f = contexto.getFazendaAtiva();
        Safra s = contexto.getSafraAtiva();

        BigDecimal custoTotal = BigDecimal.ZERO;
        BigDecimal receitaTotal = BigDecimal.ZERO;

        BigDecimal custoInsumos = BigDecimal.ZERO;
        BigDecimal custoCombustivel = BigDecimal.ZERO;
        BigDecimal custoMaoObra = BigDecimal.ZERO;
        BigDecimal custoTerceiros = BigDecimal.ZERO;
        BigDecimal custoMaquinas = BigDecimal.ZERO;

        for (OperacaoTalhao op : operacaoTalhaoRepository.findAll()) {

            if (op.getSafra() == null || !op.getSafra().getId().equals(s.getId())) continue;

            if (op.getCustoTotal() != null)
                custoTotal = custoTotal.add(op.getCustoTotal());

            if (op.getCustoInsumos() != null)
                custoInsumos = custoInsumos.add(op.getCustoInsumos());

            if (op.getCustoCombustivel() != null)
                custoCombustivel = custoCombustivel.add(op.getCustoCombustivel());

            if (op.getCustoMaoObra() != null)
                custoMaoObra = custoMaoObra.add(op.getCustoMaoObra());

            if (op.getCustoTerceiros() != null)
                custoTerceiros = custoTerceiros.add(op.getCustoTerceiros());

            if (op.getCustoMaquina() != null)
                custoMaquinas = custoMaquinas.add(op.getCustoMaquina());
        }

        BigDecimal areaTotal = BigDecimal.ZERO;
        BigDecimal producaoTotal = BigDecimal.ZERO;

        for (SafraTalhao st : safraTalhaoRepository.findAll()) {

            if (st.getSafra() == null || !st.getSafra().getId().equals(s.getId())) continue;

            if (st.getAreaUtilizada() != null)
                areaTotal = areaTotal.add(st.getAreaUtilizada());

            if (st.getProducaoReal() != null)
                producaoTotal = producaoTotal.add(st.getProducaoReal());

            if (st.getProducaoReal() != null && st.getPrecoSaca() != null)
                receitaTotal = receitaTotal.add(
                        st.getProducaoReal().multiply(st.getPrecoSaca())
                );
        }

        BigDecimal custoAdministrativo =
                administrativoRepository.sumTotalRealizado(f.getId(), s.getId());

        if (custoAdministrativo == null)
            custoAdministrativo = BigDecimal.ZERO;

        BigDecimal lucroTotal = receitaTotal.subtract(custoTotal);

        BigDecimal custoPorHectare = areaTotal.compareTo(BigDecimal.ZERO) > 0
                ? custoTotal.divide(areaTotal, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal custoPorSaca = producaoTotal.compareTo(BigDecimal.ZERO) > 0
                ? custoTotal.divide(producaoTotal, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return ComparacaoCompletaResponseDTO.builder()
                .custoTotal(custoTotal)
                .receitaTotal(receitaTotal)
                .lucroTotal(lucroTotal)
                .areaTotal(areaTotal)
                .producaoTotal(producaoTotal)
                .custoPorHectare(custoPorHectare)
                .custoPorSaca(custoPorSaca)
                .custoInsumos(custoInsumos)
                .custoCombustivel(custoCombustivel)
                .custoMaoObra(custoMaoObra)
                .custoTerceiros(custoTerceiros)
                .custoMaquinas(custoMaquinas)
                .custoAdministrativo(custoAdministrativo)
                .build();
    }

    public List<ComparacaoTalhaoResponseDTO> compararPorTalhao() {

        Fazenda f = contexto.getFazendaAtiva();
        Safra s = contexto.getSafraAtiva();

        List<ComparacaoTalhaoResponseDTO> lista = new ArrayList<>();

        BigDecimal areaTotal = BigDecimal.ZERO;
        BigDecimal producaoTotal = BigDecimal.ZERO;
        BigDecimal custoTotalFazenda = BigDecimal.ZERO;

        List<SafraTalhao> talhoes = safraTalhaoRepository.findAll();
        List<OperacaoTalhao> operacoes = operacaoTalhaoRepository.findAll();

        for (SafraTalhao st : talhoes) {

            if (st.getSafra() == null || !st.getSafra().getId().equals(s.getId())) continue;

            if (st.getAreaUtilizada() != null)
                areaTotal = areaTotal.add(st.getAreaUtilizada());

            if (st.getProducaoReal() != null)
                producaoTotal = producaoTotal.add(st.getProducaoReal());
        }

        for (OperacaoTalhao op : operacoes) {

            if (op.getSafra() == null || !op.getSafra().getId().equals(s.getId())) continue;

            if (op.getCustoTotal() != null)
                custoTotalFazenda = custoTotalFazenda.add(op.getCustoTotal());
        }

        List<Administrativo> administrativos =
                administrativoRepository.findByFazenda_IdAndSafra_Id(f.getId(), s.getId());

        for (SafraTalhao st : talhoes) {

            if (st.getSafra() == null || !st.getSafra().getId().equals(s.getId())) continue;

            BigDecimal custoTotal = BigDecimal.ZERO;

            BigDecimal custoInsumos = BigDecimal.ZERO;
            BigDecimal custoCombustivel = BigDecimal.ZERO;
            BigDecimal custoMaoObra = BigDecimal.ZERO;
            BigDecimal custoTerceiros = BigDecimal.ZERO;
            BigDecimal custoMaquinas = BigDecimal.ZERO;

            for (OperacaoTalhao op : operacoes) {

                if (op.getSafraTalhao() == null ||
                        !op.getSafraTalhao().getId().equals(st.getId())) continue;

                if (op.getCustoTotal() != null)
                    custoTotal = custoTotal.add(op.getCustoTotal());

                if (op.getCustoInsumos() != null)
                    custoInsumos = custoInsumos.add(op.getCustoInsumos());

                if (op.getCustoCombustivel() != null)
                    custoCombustivel = custoCombustivel.add(op.getCustoCombustivel());

                if (op.getCustoMaoObra() != null)
                    custoMaoObra = custoMaoObra.add(op.getCustoMaoObra());

                if (op.getCustoTerceiros() != null)
                    custoTerceiros = custoTerceiros.add(op.getCustoTerceiros());

                if (op.getCustoMaquina() != null)
                    custoMaquinas = custoMaquinas.add(op.getCustoMaquina());
            }

            BigDecimal custoAdminTalhao = BigDecimal.ZERO;

            for (Administrativo adm : administrativos) {

                BigDecimal valor = adm.getValorTotalRealizado();

                if (valor == null || valor.compareTo(BigDecimal.ZERO) == 0) continue;

                String tipo = adm.getTipoRateio() != null
                        ? adm.getTipoRateio().getDescricao()
                        : "AREA";

                BigDecimal rateado = BigDecimal.ZERO;

                switch (tipo) {

                    case "AREA":
                        if (areaTotal.compareTo(BigDecimal.ZERO) > 0) {
                            rateado = valor
                                    .divide(areaTotal, 6, RoundingMode.HALF_UP)
                                    .multiply(st.getAreaUtilizada() != null ? st.getAreaUtilizada() : BigDecimal.ZERO);
                        }
                        break;

                    case "PRODUCAO":
                        if (st.getProducaoReal() != null && producaoTotal.compareTo(BigDecimal.ZERO) > 0) {
                            rateado = valor
                                    .divide(producaoTotal, 6, RoundingMode.HALF_UP)
                                    .multiply(st.getProducaoReal());
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

            BigDecimal producao = st.getProducaoReal() != null ? st.getProducaoReal() : BigDecimal.ZERO;
            BigDecimal preco = st.getPrecoSaca() != null ? st.getPrecoSaca() : BigDecimal.ZERO;

            BigDecimal receita = producao.multiply(preco);
            BigDecimal lucro = receita.subtract(custoTotal);

            BigDecimal desvio = BigDecimal.ZERO;

            if (custoTotal.compareTo(BigDecimal.ZERO) > 0) {
                desvio = lucro.divide(custoTotal, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            lista.add(
                    ComparacaoTalhaoResponseDTO.builder()
                            .safraTalhaoId(st.getId())
                            .talhaoNome(st.getTalhao().getNome())
                            .custoReal(custoTotal)
                            .receita(receita)
                            .lucro(lucro)
                            .desvioPercentual(desvio)
                            .custoInsumos(custoInsumos)
                            .custoCombustivel(custoCombustivel)
                            .custoMaoObra(custoMaoObra)
                            .custoTerceiros(custoTerceiros)
                            .custoMaquinas(custoMaquinas)
                            .build()
            );
        }

        return lista;
    }
}