package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoMaquinaCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoMaquinaResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanejamentoMaquinaService {

    private final PlanejamentoOperacaoRepository planejamentoRepository;
    private final PlanejamentoMaquinaRepository repository;
    private final MaquinaRepository maquinaRepository;

    // 🔥 NOVO
    private final PlanejamentoCalculoService calculoService;

    /* ========================= ADICIONAR ========================= */

    public PlanejamentoMaquinaResponseDTO adicionar(Long planejamentoId, PlanejamentoMaquinaCreateDTO dto) {

        PlanejamentoOperacao planejamento = planejamentoRepository.findById(planejamentoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

        if ("EXECUTADO".equals(planejamento.getStatus())) {
            throw new AuthException("Planejamento já executado");
        }

        Maquina maquina = maquinaRepository.findById(dto.getMaquinaId())
                .orElseThrow(() -> new AuthException("Máquina não encontrada"));

        CadastroOperacao operacao = planejamento.getOperacao();

        BigDecimal area = planejamento.getAreaPlanejada();

        if (area == null || area.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Área inválida para cálculo");
        }

        /* ========================= CÁLCULOS ========================= */

        BigDecimal velocidade = operacao.getVelocidadeOp();
        BigDecimal eficiencia = operacao.getEficienciaCampo();

        if (velocidade == null || velocidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Velocidade inválida na operação");
        }

        if (eficiencia == null || eficiencia.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Eficiência inválida na operação");
        }

        // 🔥 CORREÇÃO: aceita 80 ou 0.8
        BigDecimal eficienciaDecimal =
                eficiencia.compareTo(BigDecimal.ONE) > 0
                        ? eficiencia.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)
                        : eficiencia;

        BigDecimal capacidade = velocidade.multiply(eficienciaDecimal);

        if (capacidade.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Capacidade operacional inválida");
        }

        BigDecimal horas = area.divide(capacidade, 4, RoundingMode.HALF_UP);

        /* ========================= CUSTOS ========================= */

        BigDecimal custoHora = maquina.getValorDiaria() != null
                ? maquina.getValorDiaria()
                : BigDecimal.ZERO;

        BigDecimal custoTotal = custoHora.multiply(horas);

        /* ========================= SALVAR ========================= */

        PlanejamentoMaquina p = new PlanejamentoMaquina();

        p.setPlanejamentoOperacao(planejamento);
        p.setMaquina(maquina);
        p.setHorasPrevistas(horas);
        p.setCustoHora(custoHora);
        p.setCustoTotal(custoTotal);
        p.setAtivo(true);

        p = repository.save(p);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);

        return toDTO(p);
    }

    /* ========================= LISTAR ========================= */

    public List<PlanejamentoMaquinaResponseDTO> listar(Long planejamentoId) {

        return repository.findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* ========================= REMOVER ========================= */

    public void removerMaquina(Long planejamentoId, Long itemId) {

        PlanejamentoMaquina entity = repository.findById(itemId)
                .orElseThrow(() -> new AuthException("Máquina não encontrada"));

        if (!entity.getPlanejamentoOperacao().getId().equals(planejamentoId)) {
            throw new AuthException("Máquina não pertence ao planejamento");
        }

        entity.setAtivo(false);

        repository.save(entity);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);
    }

    /* ========================= DTO ========================= */

    private PlanejamentoMaquinaResponseDTO toDTO(PlanejamentoMaquina p) {

        return PlanejamentoMaquinaResponseDTO.builder()
                .id(p.getId())
                .planejamentoOperacaoId(p.getPlanejamentoOperacao().getId())
                .maquinaId(p.getMaquina().getId())
                .horasPrevistas(p.getHorasPrevistas())
                .custoHora(p.getCustoHora())
                .custoTotal(p.getCustoTotal())
                .ativo(p.getAtivo())
                .build();
    }
}