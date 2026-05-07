package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.operacaotalhao.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OperacaoTalhaoService extends BaseCrudService<OperacaoTalhao, Long> {

    private final OperacaoTalhaoRepository repository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final SafraTalhaoRepository safraTalhaoRepository;
    private final RefOperacaoTalhaoRepository refOperacaoTalhaoRepository;

    private final OperacaoProdutoRepository operacaoProdutoRepository;
    private final OperacaoCombustivelRepository operacaoCombustivelRepository;
    private final OperacaoFuncionarioRepository operacaoFuncionarioRepository;
    private final TurmaTerceirizadaRepository turmaRepository;
    private final HoraMaquinaRepository horaMaquinaRepository;
    private final ParametroService parametroService;

    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<OperacaoTalhao, Long> getRepository() {
        return repository;
    }

    @Override
    protected void validarAcesso(OperacaoTalhao entity) {

        if (!entity.getFazenda().getId().equals(contexto.getFazendaAtiva().getId())) {
            throw new AuthException("Operação não pertence à fazenda ativa");
        }
    }

    /* =========================================================
       🔥 LISTAR
       ========================================================= */

    public PageResponseDTO<OperacaoTalhaoResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_Id(
                        contexto.getProprietario().getId(),
                        contexto.getFazendaAtiva().getId(),
                        contexto.getSafraAtiva().getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       🔥 BUSCAR
       ========================================================= */

    public OperacaoTalhaoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(super.buscarEntity(id));
    }

    /* =========================================================
       🔥 CRIAR
       ========================================================= */

    public OperacaoTalhaoResponseDTO criar(OperacaoTalhaoCreateDTO dto) {

        OrdemServico ordem = ordemServicoRepository.findById(dto.getOrdemServicoId())
                .orElseThrow(() -> new AuthException("Ordem de serviço não encontrada"));

        SafraTalhao safraTalhao = safraTalhaoRepository.findById(dto.getSafraTalhaoId())
                .orElseThrow(() -> new AuthException("SafraTalhão não encontrado"));

        RefOperacaoTalhao tipo = null;

        if (dto.getOperacaoTalhaoTipoId() != null) {

            tipo = refOperacaoTalhaoRepository.findById(dto.getOperacaoTalhaoTipoId())
                    .orElseThrow(() -> new AuthException("Tipo de operação não encontrado"));
        }

        OperacaoTalhao op = new OperacaoTalhao();

        op.setOrdemServico(ordem);
        op.setSafraTalhao(safraTalhao);
        op.setOperacaoTalhaoTipo(tipo);

        op.setAreaTrabalhada(dto.getAreaTrabalhada());
        op.setDataExecucao(dto.getDataExecucao());

        op.setProprietario(contexto.getProprietario());
        op.setFazenda(contexto.getFazendaAtiva());
        op.setSafra(contexto.getSafraAtiva());

        op = super.salvarEntity(op);

        return toResponseDTO(op);
    }

    /* =========================================================
       🔥 ATUALIZAR
       ========================================================= */

    public OperacaoTalhaoResponseDTO atualizar(Long id, OperacaoTalhaoUpdateDTO dto) {

        OperacaoTalhao op = super.buscarEntity(id);

        op.setAreaTrabalhada(dto.getAreaTrabalhada());
        op.setDataExecucao(dto.getDataExecucao());

        op = super.salvarEntity(op);

        return toResponseDTO(op);
    }

    /* =========================================================
       🔥 EXCLUIR
       ========================================================= */

    public void excluir(Long id) {

        OperacaoTalhao op = super.buscarEntity(id);

        repository.delete(op);
    }

    /* =========================================================
       🔥 RECÁLCULO (CORAÇÃO DO SISTEMA)
       ========================================================= */

    public void recalcularCusto(Long operacaoId) {

        OperacaoTalhao op = repository.findById(operacaoId)
                .orElseThrow(() -> new RuntimeException("Operação não encontrada"));

        BigDecimal custoInsumos =
                operacaoProdutoRepository.sumByOperacao(operacaoId);

        BigDecimal custoCombustivel =
                operacaoCombustivelRepository.sumByOperacao(operacaoId);

        BigDecimal percentualEncargos = parametroService.getPercentualEncargos();

        BigDecimal custoMaoObra =
                operacaoFuncionarioRepository.sumByOperacao(
                        operacaoId,
                        percentualEncargos
                );

        BigDecimal custoTerceiros =
                turmaRepository.sumByOperacao(operacaoId);

        BigDecimal custoMaquina =
                horaMaquinaRepository.sumByOperacao(operacaoId);

        if (custoInsumos == null) custoInsumos = BigDecimal.ZERO;
        if (custoCombustivel == null) custoCombustivel = BigDecimal.ZERO;
        if (custoMaoObra == null) custoMaoObra = BigDecimal.ZERO;
        if (custoTerceiros == null) custoTerceiros = BigDecimal.ZERO;
        if (custoMaquina == null) custoMaquina = BigDecimal.ZERO;

        BigDecimal custoTotal =
                custoInsumos
                        .add(custoCombustivel)
                        .add(custoMaoObra)
                        .add(custoTerceiros)
                        .add(custoMaquina);

        op.setCustoInsumos(custoInsumos);
        op.setCustoCombustivel(custoCombustivel);
        op.setCustoMaoObra(custoMaoObra);
        op.setCustoTerceiros(custoTerceiros);
        op.setCustoMaquina(custoMaquina);
        op.setCustoTotal(custoTotal);

        repository.save(op);
    }

    /* =========================================================
       🔥 RESPONSE
       ========================================================= */

    private OperacaoTalhaoResponseDTO toResponseDTO(OperacaoTalhao op) {

        return OperacaoTalhaoResponseDTO.builder()
                .id(op.getId())

                .proprietarioId(
                        op.getProprietario() != null
                                ? op.getProprietario().getId()
                                : null
                )

                .fazendaId(
                        op.getFazenda() != null
                                ? op.getFazenda().getId()
                                : null
                )

                .safraId(
                        op.getSafra() != null
                                ? op.getSafra().getId()
                                : null
                )

                .ordemServicoId(
                        op.getOrdemServico() != null
                                ? op.getOrdemServico().getId()
                                : null
                )

                .safraTalhaoId(
                        op.getSafraTalhao() != null
                                ? op.getSafraTalhao().getId()
                                : null
                )

                .talhaoId(
                        op.getSafraTalhao() != null
                                ? op.getSafraTalhao().getTalhao().getId()
                                : null
                )

                .talhaoNome(
                        op.getSafraTalhao() != null
                                ? op.getSafraTalhao().getTalhao().getNome()
                                : null
                )

                .operacaoTalhaoTipoId(
                        op.getOperacaoTalhaoTipo() != null
                                ? op.getOperacaoTalhaoTipo().getId()
                                : null
                )

                .operacaoTalhaoTipoDescricao(
                        op.getOperacaoTalhaoTipo() != null
                                ? op.getOperacaoTalhaoTipo().getDescricao()
                                : null
                )

                .ordemServicoStatus(
                        op.getOrdemServico() != null
                                ? op.getOrdemServico().getStatus()
                                : null
                )

                .areaTrabalhada(op.getAreaTrabalhada())
                .dataExecucao(op.getDataExecucao())

                .custoTotal(op.getCustoTotal())
                .custoInsumos(op.getCustoInsumos())
                .custoCombustivel(op.getCustoCombustivel())

                .build();
    }
}