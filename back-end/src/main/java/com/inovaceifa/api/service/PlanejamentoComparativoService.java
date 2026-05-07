package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoComparativoDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoResumoDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.OrdemServico;
import com.inovaceifa.api.repository.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class PlanejamentoComparativoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final PlanejamentoCalculoService calculoService;

    public PlanejamentoComparativoDTO comparar(Long planejamentoOperacaoId) {

        PlanejamentoResumoDTO resumo = calculoService.gerarResumo(planejamentoOperacaoId);

        BigDecimal custoPrevisto = resumo.getCustoTotal();

        OrdemServico os = ordemServicoRepository
                .findByPlanejamentoOperacaoId(planejamentoOperacaoId)
                .orElseThrow(() -> new AuthException("Ordem de serviço não encontrada para esse planejamento"));

        BigDecimal custoReal = os.getCustoTotal() != null
                ? os.getCustoTotal()
                : BigDecimal.ZERO;

        BigDecimal diferenca = custoReal.subtract(custoPrevisto);

        BigDecimal percentual = BigDecimal.ZERO;

        if (custoPrevisto.compareTo(BigDecimal.ZERO) > 0) {
            percentual = diferenca
                    .divide(custoPrevisto, 6, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return PlanejamentoComparativoDTO.builder()
                .planejamentoOperacaoId(planejamentoOperacaoId)
                .custoPrevisto(custoPrevisto)
                .custoRealizado(custoReal)
                .diferencaValor(diferenca)
                .diferencaPercentual(percentual)
                .build();
    }
}