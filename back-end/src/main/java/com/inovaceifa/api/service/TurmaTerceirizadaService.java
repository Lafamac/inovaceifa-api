package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.turma.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.TurmaTerceirizadaRepository;
import com.inovaceifa.api.repository.RefTipoPagamentoRepository;
import com.inovaceifa.api.repository.CadastroOperacaoRepository;
import com.inovaceifa.api.repository.OperacaoTalhaoRepository; // 🔥 NOVO
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TurmaTerceirizadaService extends BaseCrudService<TurmaTerceirizada, Long> {

    private final TurmaTerceirizadaRepository repository;
    private final RefTipoPagamentoRepository tipoPagamentoRepository;
    private final CadastroOperacaoRepository operacaoRepository;
    private final OperacaoTalhaoRepository operacaoTalhaoRepository; // 🔥 NOVO
    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<TurmaTerceirizada, Long> getRepository() {
        return repository;
    }

    @Override
    protected void validarAcesso(TurmaTerceirizada t) {

        Proprietario proprietario = contexto.getProprietario();

        if (proprietario == null) {
            throw new AuthException("Proprietário não encontrado no contexto");
        }

        if (!t.getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Turma não pertence ao proprietário ativo");
        }
    }

    public PageResponseDTO<TurmaResponseDTO> listar(Pageable pageable) {

        Proprietario proprietario = contexto.getProprietario();

        if (proprietario == null) {
            throw new AuthException("Proprietário não encontrado no contexto");
        }

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndAtivoTrue(proprietario.getId(), pageable),
                this::toResponse
        );
    }

    public PageResponseDTO<TurmaResponseDTO> listarInativas(Pageable pageable) {

        Proprietario proprietario = contexto.getProprietario();

        if (proprietario == null) {
            throw new AuthException("Proprietário não encontrado no contexto");
        }

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndAtivoFalse(proprietario.getId(), pageable),
                this::toResponse
        );
    }

    public TurmaResponseDTO buscar(Long id) {
        return toResponse(super.buscarEntity(id));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public TurmaResponseDTO criar(TurmaCreateDTO dto) {

        RefTipoPagamento tipoPagamento = tipoPagamentoRepository.findById(dto.getTipoPagamentoId())
                .orElseThrow(() -> new AuthException("Tipo de pagamento não encontrado"));

        TurmaTerceirizada t = new TurmaTerceirizada();

        t.setNome(dto.getNome());
        t.setResponsavel(dto.getResponsavel());
        t.setTipoPagamento(tipoPagamento);
        t.setValorDiaria(dto.getValorDiaria());
        t.setValorPorSaca(dto.getValorPorSaca());
        t.setQuantidadePessoas(dto.getQuantidadePessoas());
        t.setDataInicio(dto.getDataInicio());
        t.setDataFim(dto.getDataFim());
        t.setAtivo(true);

        /* 🔥 MANTIDO - OPERACAO (CADASTRO) */
        if (dto.getOperacaoId() != null) {
            CadastroOperacao operacao = operacaoRepository.findById(dto.getOperacaoId())
                    .orElseThrow(() -> new AuthException("Operação não encontrada"));
            t.setOperacao(operacao);
        }

        /* 🔥 NOVO - VÍNCULO COM OPERACAO TALHAO */
        if (dto.getOperacaoTalhaoId() != null) {
            OperacaoTalhao operacaoTalhao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                    .orElseThrow(() -> new AuthException("Operação do talhão não encontrada"));
            t.setOperacaoTalhao(operacaoTalhao);
        }

        t.setProprietario(contexto.getProprietario());
        t.setSafra(contexto.getSafraAtiva());

        t = super.salvarEntity(t);

        return toResponse(t);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public TurmaResponseDTO atualizar(Long id, TurmaUpdateDTO dto) {

        TurmaTerceirizada t = super.buscarEntity(id);

        if (dto.getNome() != null) t.setNome(dto.getNome());
        if (dto.getResponsavel() != null) t.setResponsavel(dto.getResponsavel());

        if (dto.getTipoPagamentoId() != null) {

            RefTipoPagamento tipoPagamento = tipoPagamentoRepository
                    .findById(dto.getTipoPagamentoId())
                    .orElseThrow(() -> new AuthException("Tipo de pagamento não encontrado"));

            t.setTipoPagamento(tipoPagamento);
        }

        /* 🔥 MANTIDO */
        if (dto.getOperacaoId() != null) {
            CadastroOperacao operacao = operacaoRepository.findById(dto.getOperacaoId())
                    .orElseThrow(() -> new AuthException("Operação não encontrada"));
            t.setOperacao(operacao);
        }

        /* 🔥 NOVO */
        if (dto.getOperacaoTalhaoId() != null) {
            OperacaoTalhao operacaoTalhao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                    .orElseThrow(() -> new AuthException("Operação do talhão não encontrada"));
            t.setOperacaoTalhao(operacaoTalhao);
        }

        if (dto.getValorDiaria() != null) t.setValorDiaria(dto.getValorDiaria());
        if (dto.getValorPorSaca() != null) t.setValorPorSaca(dto.getValorPorSaca());
        if (dto.getQuantidadePessoas() != null) t.setQuantidadePessoas(dto.getQuantidadePessoas());
        if (dto.getDataInicio() != null) t.setDataInicio(dto.getDataInicio());
        if (dto.getDataFim() != null) t.setDataFim(dto.getDataFim());

        t = super.salvarEntity(t);

        return toResponse(t);
    }

    public void excluir(Long id) {

        TurmaTerceirizada t = super.buscarEntity(id);
        t.setAtivo(false);
        super.salvarEntity(t);
    }

    public void reativar(Long id) {

        TurmaTerceirizada t = super.buscarEntity(id);
        t.setAtivo(true);
        super.salvarEntity(t);
    }

    private TurmaResponseDTO toResponse(TurmaTerceirizada t) {

        return TurmaResponseDTO.builder()
                .id(t.getId())
                .nome(t.getNome())
                .responsavel(t.getResponsavel())
                .tipoPagamentoId(
                        t.getTipoPagamento() != null ? t.getTipoPagamento().getId() : null
                )
                .descricaoTipoPagamento(
                        t.getTipoPagamento() != null ? t.getTipoPagamento().getDescricao() : null
                )
                .operacaoId(
                        t.getOperacao() != null ? t.getOperacao().getId() : null
                )
                .valorDiaria(t.getValorDiaria())
                .valorPorSaca(t.getValorPorSaca())
                .quantidadePessoas(t.getQuantidadePessoas())
                .dataInicio(t.getDataInicio())
                .dataFim(t.getDataFim())
                .ativo(t.getAtivo())
                .build();
    }
}