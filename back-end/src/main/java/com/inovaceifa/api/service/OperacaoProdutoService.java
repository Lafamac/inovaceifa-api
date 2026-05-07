package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.operacaoproduto.*;
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
public class OperacaoProdutoService extends BaseCrudService<OperacaoProduto, Long> {

    private final OperacaoProdutoRepository repository;
    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final ProdutoRepository produtoRepository;

    // 🔥 NOVO
    private final OperacaoTalhaoService operacaoTalhaoService;

    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<OperacaoProduto, Long> getRepository() {
        return repository;
    }

    @Override
    protected void validarAcesso(OperacaoProduto entity) {

        if (!entity.getFazenda().getId().equals(contexto.getFazendaAtiva().getId())) {
            throw new AuthException("Produto não pertence à fazenda ativa");
        }
    }

    public PageResponseDTO<OperacaoProdutoResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_Id(
                        contexto.getProprietario().getId(),
                        contexto.getFazendaAtiva().getId(),
                        contexto.getSafraAtiva().getId(),
                        pageable
                ),
                this::toResponse
        );
    }

    public OperacaoProdutoResponseDTO criar(OperacaoProdutoCreateDTO dto) {

        OperacaoTalhao operacao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new AuthException("Produto não encontrado"));

        OperacaoProduto op = new OperacaoProduto();

        op.setOperacaoTalhao(operacao);
        op.setProduto(produto);
        op.setQuantidade(dto.getQuantidade());
        op.setVlrUnitario(dto.getVlrUnitario());

        if (dto.getQuantidade() != null && dto.getVlrUnitario() != null) {
            op.setVlrTotal(dto.getQuantidade().multiply(dto.getVlrUnitario()));
        }

        op.setProprietario(contexto.getProprietario());
        op.setFazenda(contexto.getFazendaAtiva());
        op.setSafra(contexto.getSafraAtiva());

        op = super.salvarEntity(op);

        // 🔥 RECALCULAR
        operacaoTalhaoService.recalcularCusto(dto.getOperacaoTalhaoId());

        return toResponse(op);
    }

    private OperacaoProdutoResponseDTO toResponse(OperacaoProduto op) {

        return OperacaoProdutoResponseDTO.builder()
                .id(op.getId())
                .operacaoTalhaoId(op.getOperacaoTalhao().getId())
                .produtoId(op.getProduto().getId())
                .produtoNome(op.getProduto().getNome())
                .quantidade(op.getQuantidade())
                .vlrUnitario(op.getVlrUnitario())
                .vlrTotal(op.getVlrTotal())
                .build();
    }
}