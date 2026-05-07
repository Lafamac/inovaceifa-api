package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoComparativoDetalhadoDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoResumoDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.OrdemServico;
import com.inovaceifa.api.repository.OrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PlanejamentoComparativoDetalhadoService {

    private final PlanejamentoCalculoService calculoService;
    private final OrdemServicoRepository ordemServicoRepository;
    private final OrdemServicoCalculoService ordemCalculoService;

    public PlanejamentoComparativoDetalhadoDTO compararDetalhado(Long planejamentoOperacaoId) {

        /* =========================
           PREVISTO (PLANEJAMENTO)
           ========================= */

        PlanejamentoResumoDTO previsto =
                calculoService.gerarResumo(planejamentoOperacaoId);

        /* =========================
           REAL (ORDEM DE SERVIÇO)
           ========================= */

        OrdemServico os = ordemServicoRepository
                .findByPlanejamentoOperacaoId(planejamentoOperacaoId)
                .orElseThrow(() -> new AuthException("Ordem de serviço não encontrada"));

        BigDecimal precoDiesel = BigDecimal.valueOf(6.50);

        ResultadoCalculoOS real =
                ordemCalculoService.calcularResumoCompleto(os.getId(), precoDiesel);

        /* =========================
           RESULTADO FINAL
           ========================= */

        return PlanejamentoComparativoDetalhadoDTO.builder()
                .planejamentoOperacaoId(planejamentoOperacaoId)

                .previstoInsumos(previsto.getCustoInsumos())
                .realInsumos(real.getInsumos())

                .previstoMaquina(previsto.getCustoMaquina())
                .realMaquina(real.getMaquinas())

                .previstoMaoObra(previsto.getCustoMaoObra())
                .realMaoObra(real.getMaoObra())

                .previstoCombustivel(previsto.getCustoCombustivel())
                .realCombustivel(real.getCombustivel())

                .previstoTotal(previsto.getCustoTotal())
                .realTotal(real.getTotal())

                .build();
    }
}