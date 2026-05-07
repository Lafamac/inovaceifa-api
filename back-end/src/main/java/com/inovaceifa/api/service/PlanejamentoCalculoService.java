package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoResumoDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.CadastroOperacao;
import com.inovaceifa.api.model.PlanejamentoOperacao;
import com.inovaceifa.api.model.SafraTalhao;
import com.inovaceifa.api.repository.PlanejamentoFuncionarioRepository;
import com.inovaceifa.api.repository.PlanejamentoInsumoRepository;
import com.inovaceifa.api.repository.PlanejamentoMaquinaRepository;
import com.inovaceifa.api.repository.PlanejamentoOperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanejamentoCalculoService {

    private final PlanejamentoOperacaoRepository planejamentoRepository;
    private final PlanejamentoInsumoRepository planejamentoInsumoRepository;
    private final PlanejamentoFuncionarioRepository planejamentoFuncionarioRepository;
    private final PlanejamentoMaquinaRepository planejamentoMaquinaRepository;

    /* =========================
       RESUMO (GET)
       ========================= */

    @Transactional(readOnly = true)
    public PlanejamentoResumoDTO gerarResumo(Long planejamentoOperacaoId) {

        PlanejamentoOperacao planejamento = planejamentoRepository.findById(planejamentoOperacaoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

        BigDecimal area = planejamento.getAreaPlanejada();

        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Área planejada inválida");
        }

        BigDecimal custoInsumos = somarInsumos(planejamentoOperacaoId);
        BigDecimal custoMaquina = somarMaquinas(planejamentoOperacaoId);
        BigDecimal custoMaoObra = somarMaoObra(planejamentoOperacaoId);
        BigDecimal custoCombustivel = calcularCombustivel(planejamento, area);

        BigDecimal custoTotal =
                custoInsumos
                        .add(custoMaquina)
                        .add(custoMaoObra)
                        .add(custoCombustivel);

        SafraTalhao safraTalhao = planejamento.getSafraTalhao();

        if (safraTalhao == null) {
            throw new AuthException("Safra talhão não encontrada");
        }

        BigDecimal producao = safraTalhao.getEstimativaSaca();

        BigDecimal custoPorHa = custoTotal.divide(area, 6, RoundingMode.HALF_UP);

        BigDecimal custoPorSaca = BigDecimal.ZERO;

        if (producao != null && producao.compareTo(BigDecimal.ZERO) > 0) {
            custoPorSaca = custoTotal.divide(producao, 6, RoundingMode.HALF_UP);
        }

        /* =========================
           🔥 INSUMOS AGRUPADOS (CORRIGIDO)
           ========================= */

        Map<String, BigDecimal> insumosAgrupados =
                planejamentoInsumoRepository
                        .findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoOperacaoId)
                        .stream()
                        .collect(Collectors.groupingBy(
                                i -> i.getProduto().getNome(),
                                Collectors.reducing(
                                        BigDecimal.ZERO,
                                        i -> i.getQuantidadeTotal() != null
                                                ? i.getQuantidadeTotal()
                                                : BigDecimal.ZERO,
                                        BigDecimal::add
                                )
                        ));

        List<PlanejamentoResumoDTO.InsumoDTO> insumos = insumosAgrupados.entrySet()
                .stream()
                .map(e -> PlanejamentoResumoDTO.InsumoDTO.builder()
                        .produtoNome(e.getKey())
                        .quantidadeTotal(e.getValue())
                        .build())
                .toList();

        return PlanejamentoResumoDTO.builder()
                .planejamentoOperacaoId(planejamentoOperacaoId)
                .areaPlanejada(area)
                .custoInsumos(custoInsumos)
                .custoMaquina(custoMaquina)
                .custoMaoObra(custoMaoObra)
                .custoCombustivel(custoCombustivel)
                .custoTotal(custoTotal)
                .custoPorHectare(custoPorHa)
                .custoPorSaca(custoPorSaca)
                .insumos(insumos) // agora funciona
                .build();
    }

    /* =========================
       🔥 RECALCULAR E SALVAR
       ========================= */

    @Transactional
    public void recalcularEAtualizar(Long planejamentoOperacaoId) {

        PlanejamentoOperacao planejamento = planejamentoRepository.findById(planejamentoOperacaoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

        BigDecimal area = planejamento.getAreaPlanejada();

        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Área planejada inválida");
        }

        BigDecimal custoInsumos = somarInsumos(planejamentoOperacaoId);
        BigDecimal custoMaquinas = somarMaquinas(planejamentoOperacaoId);
        BigDecimal custoMaoObra = somarMaoObra(planejamentoOperacaoId);
        BigDecimal custoCombustivel = calcularCombustivel(planejamento, area);

        BigDecimal custoTotal =
                custoInsumos
                        .add(custoMaquinas)
                        .add(custoMaoObra)
                        .add(custoCombustivel);

        planejamento.setCustoInsumos(custoInsumos);
        planejamento.setCustoMaquinas(custoMaquinas);
        planejamento.setCustoCombustivel(custoCombustivel);
        planejamento.setCustoTotal(custoTotal);

        planejamentoRepository.save(planejamento);
    }

    /* =========================
       MÉTODOS AUXILIARES
       ========================= */

    private BigDecimal somarInsumos(Long planejamentoId) {
        return planejamentoInsumoRepository
                .findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(i -> i.getValorTotalPrevisto() != null
                        ? i.getValorTotalPrevisto()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarMaquinas(Long planejamentoId) {
        return planejamentoMaquinaRepository
                .findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(m -> m.getCustoTotal() != null
                        ? m.getCustoTotal()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarMaoObra(Long planejamentoId) {
        return planejamentoFuncionarioRepository
                .findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(f -> f.getCustoTotalPrevisto() != null
                        ? f.getCustoTotalPrevisto()
                        : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calcularCombustivel(PlanejamentoOperacao planejamento, BigDecimal area) {

        CadastroOperacao op = planejamento.getOperacao();

        BigDecimal gastoDiesel = op.getGastoDiesel() != null
                ? op.getGastoDiesel()
                : BigDecimal.ZERO;

        BigDecimal litros = gastoDiesel.multiply(area);

        BigDecimal valorDiesel = BigDecimal.valueOf(6.50);

        return litros.multiply(valorDiesel);
    }
}