package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoFuncionarioCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoFuncionarioResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.PlanejamentoFuncionario;
import com.inovaceifa.api.model.PlanejamentoOperacao;
import com.inovaceifa.api.model.Funcionario;
import com.inovaceifa.api.model.Terceirizado;
import com.inovaceifa.api.model.TurmaTerceirizada;
import com.inovaceifa.api.repository.PlanejamentoFuncionarioRepository;
import com.inovaceifa.api.repository.PlanejamentoOperacaoRepository;
import com.inovaceifa.api.repository.FuncionarioRepository;
import com.inovaceifa.api.repository.TerceirizadoRepository;
import com.inovaceifa.api.repository.TurmaTerceirizadaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanejamentoFuncionarioService {

    private final PlanejamentoFuncionarioRepository repository;
    private final PlanejamentoOperacaoRepository planejamentoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TerceirizadoRepository terceirizadoRepository;
    private final TurmaTerceirizadaRepository turmaRepository;

    // 🔥 NOVO
    private final PlanejamentoCalculoService calculoService;

    @Transactional
    public PlanejamentoFuncionarioResponseDTO criar(Long planejamentoId,
                                                    PlanejamentoFuncionarioCreateDTO dto) {

        PlanejamentoOperacao planejamento = planejamentoRepository.findById(planejamentoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

        validarTipo(dto);

        PlanejamentoFuncionario entity = new PlanejamentoFuncionario();

        entity.setPlanejamentoOperacao(planejamento);
        entity.setTipoMaoObra(dto.getTipoMaoObra());
        entity.setQuantidadePessoas(
                dto.getQuantidadePessoas() != null ? dto.getQuantidadePessoas() : 1L
        );
        entity.setHorasPrevistas(dto.getHorasPrevistas());
        entity.setCustoHoraPrevisto(dto.getCustoHoraPrevisto());
        entity.setObservacao(dto.getObservacao());
        entity.setAtivo(true);

        /* ========================= VINCULAÇÃO ========================= */

        if ("FUNCIONARIO".equalsIgnoreCase(dto.getTipoMaoObra())) {

            Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                    .orElseThrow(() -> new AuthException("Funcionário não encontrado"));

            entity.setFuncionario(funcionario);
        }

        if ("TERCEIRIZADO".equalsIgnoreCase(dto.getTipoMaoObra())) {

            Terceirizado terceirizado = terceirizadoRepository.findById(dto.getTerceirizadoId())
                    .orElseThrow(() -> new AuthException("Terceirizado não encontrado"));

            entity.setTerceirizado(terceirizado);
        }

        if ("TURMA".equalsIgnoreCase(dto.getTipoMaoObra())) {

            TurmaTerceirizada turma = turmaRepository.findById(dto.getTurmaId())
                    .orElseThrow(() -> new AuthException("Turma não encontrada"));

            entity.setTurma(turma);
        }

        /* ========================= CÁLCULO ========================= */

        BigDecimal horas = dto.getHorasPrevistas() != null ? dto.getHorasPrevistas() : BigDecimal.ZERO;
        BigDecimal custoHora = dto.getCustoHoraPrevisto() != null ? dto.getCustoHoraPrevisto() : BigDecimal.ZERO;

        BigDecimal quantidade = entity.getQuantidadePessoas() != null
                ? BigDecimal.valueOf(entity.getQuantidadePessoas())
                : BigDecimal.ONE;

        BigDecimal custoTotal = horas.multiply(custoHora).multiply(quantidade);

        entity.setCustoTotalPrevisto(custoTotal);

        entity = repository.save(entity);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);

        return toResponse(entity);
    }

    public List<PlanejamentoFuncionarioResponseDTO> listar(Long planejamentoId) {

        return repository.findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /* =========================================================
       VALIDAÇÃO
       ========================================================= */

    private void validarTipo(PlanejamentoFuncionarioCreateDTO dto) {

        int count = 0;

        if (dto.getFuncionarioId() != null) count++;
        if (dto.getTerceirizadoId() != null) count++;
        if (dto.getTurmaId() != null) count++;

        if (count != 1) {
            throw new AuthException("Informe apenas um tipo de mão de obra");
        }

        if ("FUNCIONARIO".equalsIgnoreCase(dto.getTipoMaoObra()) && dto.getFuncionarioId() == null) {
            throw new AuthException("Funcionário obrigatório");
        }

        if ("TERCEIRIZADO".equalsIgnoreCase(dto.getTipoMaoObra()) && dto.getTerceirizadoId() == null) {
            throw new AuthException("Terceirizado obrigatório");
        }

        if ("TURMA".equalsIgnoreCase(dto.getTipoMaoObra()) && dto.getTurmaId() == null) {
            throw new AuthException("Turma obrigatória");
        }
    }

    public void remover(Long planejamentoId, Long itemId) {

        PlanejamentoFuncionario entity = repository.findById(itemId)
                .orElseThrow(() -> new AuthException("Funcionário não encontrado"));

        if (!entity.getPlanejamentoOperacao().getId().equals(planejamentoId)) {
            throw new AuthException("Funcionário não pertence ao planejamento");
        }

        entity.setAtivo(false);

        repository.save(entity);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private PlanejamentoFuncionarioResponseDTO toResponse(PlanejamentoFuncionario e) {

        return PlanejamentoFuncionarioResponseDTO.builder()
                .id(e.getId())
                .tipoMaoObra(e.getTipoMaoObra())
                .funcionarioId(e.getFuncionario() != null ? e.getFuncionario().getId() : null)
                .terceirizadoId(e.getTerceirizado() != null ? e.getTerceirizado().getId() : null)
                .turmaId(e.getTurma() != null ? e.getTurma().getId() : null)
                .quantidadePessoas(e.getQuantidadePessoas())
                .horasPrevistas(e.getHorasPrevistas())
                .custoHoraPrevisto(e.getCustoHoraPrevisto())
                .custoTotalPrevisto(e.getCustoTotalPrevisto())
                .observacao(e.getObservacao())
                .build();
    }
}