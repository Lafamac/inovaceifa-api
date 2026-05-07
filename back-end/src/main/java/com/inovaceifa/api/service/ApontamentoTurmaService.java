package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.lancamento.LancamentoCreateDTO;
import com.inovaceifa.api.dto.apontamento.ApontamentoTurmaCreateDTO;
import com.inovaceifa.api.dto.apontamento.ApontamentoTurmaResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.ApontamentoTurmaRepository;
import com.inovaceifa.api.repository.TurmaTerceirizadaRepository;
import com.inovaceifa.api.repository.OrdemServicoRepository; // 🔥 NOVO
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApontamentoTurmaService {

    private final ApontamentoTurmaRepository repository;
    private final TurmaTerceirizadaRepository turmaRepository;
    private final LancamentoDespesaService lancamentoService;
    private final ContextoFazendaService contexto;

    // 🔥 NOVO
    private final OrdemServicoRepository ordemServicoRepository;

    @Transactional
    public ApontamentoTurmaResponseDTO registrar(ApontamentoTurmaCreateDTO dto) {

        TurmaTerceirizada turma = turmaRepository.findById(dto.getTurmaId())
                .orElseThrow(() -> new AuthException("Turma não encontrada"));

        // 🔥 NOVO
        OrdemServico os = ordemServicoRepository.findById(dto.getOrdemServicoId())
                .orElseThrow(() -> new AuthException("OS não encontrada"));

        BigDecimal valorTotal;

        String tipoPagamentoDescricao = turma.getTipoPagamento().getDescricao();

        if ("DIARIA".equalsIgnoreCase(tipoPagamentoDescricao)) {

            if (dto.getDiasTrabalhados() == null)
                throw new AuthException("Dias trabalhados obrigatório");

            valorTotal = turma.getValorDiaria()
                    .multiply(BigDecimal.valueOf(turma.getQuantidadePessoas()))
                    .multiply(BigDecimal.valueOf(dto.getDiasTrabalhados()));

        } else {

            if (dto.getQuantidadeColhida() == null)
                throw new AuthException("Quantidade colhida obrigatória");

            valorTotal = turma.getValorPorSaca()
                    .multiply(dto.getQuantidadeColhida());
        }

        ApontamentoTurma ap = new ApontamentoTurma();
        ap.setTurma(turma);
        ap.setFazenda(contexto.getFazendaAtiva());
        ap.setSafra(contexto.getSafraAtiva());

        // 🔥 NOVO
        ap.setOrdemServico(os);

        ap.setDataInicio(dto.getDataInicio());
        ap.setDataFim(dto.getDataFim());
        ap.setDiasTrabalhados(dto.getDiasTrabalhados());
        ap.setQuantidadeColhida(dto.getQuantidadeColhida());
        ap.setValorTotal(valorTotal);
        ap.setObservacao(dto.getObservacao());

        ap = repository.save(ap);

        /* =========================
           💰 GERA DESPESA AUTOMÁTICA
           ========================= */

        LancamentoCreateDTO lanc = new LancamentoCreateDTO();
        lanc.setValor(valorTotal);
        lanc.setData(LocalDate.now());

        // 🔥 ALTERADO
        lanc.setObservacao("Turma: " + turma.getNome() + " | OS #" + os.getId());

        lanc.setRefDespesaId(1L);
        lanc.setCentroCustoId(1L);

        lancamentoService.criar(lanc);

        return toResponse(ap);
    }

    /* =========================
       🔥 NOVO MÉTODO
       ========================= */

    public List<ApontamentoTurmaResponseDTO> listarPorOs(Long osId) {

        return repository.findByOrdemServicoId(osId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /* =========================
       (mantido se já existe)
       ========================= */

    public List<ApontamentoTurmaResponseDTO> listarPorSafra() {

        Fazenda fazenda = contexto.getFazendaAtiva();
        Safra safra = contexto.getSafraAtiva();

        return repository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId()
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ApontamentoTurmaResponseDTO toResponse(ApontamentoTurma ap) {

        return ApontamentoTurmaResponseDTO.builder()
                .id(ap.getId())
                .turmaId(ap.getTurma().getId())
                .turmaNome(ap.getTurma().getNome())
                .dataInicio(ap.getDataInicio())
                .dataFim(ap.getDataFim())
                .diasTrabalhados(ap.getDiasTrabalhados())
                .quantidadeColhida(ap.getQuantidadeColhida())
                .valorTotal(ap.getValorTotal())
                .observacao(ap.getObservacao())
                .build();
    }
}
