package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.produto.MovProdutoRequestDTO;
import com.inovaceifa.api.dto.produto.MovProdutoResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.MovProdutoRepository;
import com.inovaceifa.api.repository.ProdutoRepository;
import com.inovaceifa.api.repository.RefTipoMovProdutoRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovProdutoService extends BaseCrudService<MovProduto, Long> {

    private final MovProdutoRepository movProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final RefTipoMovProdutoRepository refTipoMovProdutoRepository;
    private final ContextoFazendaService contextoFazendaService;
    private final ContaPagarService contaPagarService;

    @Override
    protected JpaRepository<MovProduto, Long> getRepository() {
        return movProdutoRepository;
    }

    @Override
    protected void validarAcesso(MovProduto mov) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!mov.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Movimentação não pertence à fazenda ativa");
        }
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public List<MovProdutoResponseDTO> listar() {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return movProdutoRepository
                .findByFazendaIdAndSafraId(fazenda.getId(), safra.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PageResponseDTO<MovProdutoResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return PageUtils.toPageResponse(
                movProdutoRepository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       CRIAR MOVIMENTAÇÃO (CORRIGIDO)
       ========================================================= */

    @Transactional
    public MovProdutoResponseDTO criar(MovProdutoRequestDTO dto) {

        if (dto.getQtde() == null || dto.getQtde().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Quantidade deve ser maior que zero");
        }

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new AuthException("Produto não encontrado"));

        if (!produto.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Produto não pertence à fazenda ativa");
        }

        RefTipoMovProduto tipoMov = refTipoMovProdutoRepository
                .findById(dto.getTipoMovimentoId())
                .orElseThrow(() -> new AuthException("Tipo de movimentação inválido"));

        boolean isEntrada = "ENTRADA".equalsIgnoreCase(tipoMov.getDescricao());
        boolean isSaida = "SAIDA".equalsIgnoreCase(tipoMov.getDescricao());

        if (!isEntrada && !isSaida) {
            throw new AuthException("Tipo de movimentação inválido");
        }

        /* =====================================================
           VALIDAÇÕES
           ===================================================== */

        if (isEntrada && dto.getVlrUnitario() == null) {
            throw new AuthException("Valor unitário é obrigatório para ENTRADA");
        }

        if (isEntrada && dto.getDataPagamento() == null) {
            throw new AuthException("Data de pagamento obrigatória para ENTRADA");
        }

        BigDecimal valorUnitario =
                dto.getVlrUnitario() != null ? dto.getVlrUnitario() : BigDecimal.ZERO;

        BigDecimal total = dto.getQtde().multiply(valorUnitario);

        if (isEntrada && total.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Valor total inválido");
        }

        /* =====================================================
           CRIA MOVIMENTAÇÃO
           ===================================================== */

        MovProduto mov = new MovProduto();

        mov.setProduto(produto);
        mov.setFazenda(fazenda);
        mov.setSafra(safra);
        mov.setTipoMovimento(tipoMov);
        mov.setDataMovimento(dto.getDataMovimento());
        mov.setQtde(dto.getQtde());
        mov.setVlrUnitario(valorUnitario);
        mov.setVlrTotal(total);
        mov.setNumeroNotaFiscal(dto.getNumeroNotaFiscal());
        mov.setNumeroOrdemServico(dto.getNumeroOrdemServico());
        mov.setDataPagamento(dto.getDataPagamento());

        mov = super.salvarEntity(mov);

        /* =====================================================
           ATUALIZA ESTOQUE + CUSTO MÉDIO
           ===================================================== */

        BigDecimal estoqueAtual = produto.getQtde() != null ? produto.getQtde() : BigDecimal.ZERO;
        BigDecimal custoAtual = produto.getPrecoCusto() != null ? produto.getPrecoCusto() : BigDecimal.ZERO;

        if (isEntrada) {

            BigDecimal novoTotal =
                    estoqueAtual.multiply(custoAtual)
                            .add(dto.getQtde().multiply(valorUnitario));

            BigDecimal novaQtde = estoqueAtual.add(dto.getQtde());

            BigDecimal novoCustoMedio = novaQtde.compareTo(BigDecimal.ZERO) > 0
                    ? novoTotal.divide(novaQtde, 4, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;

            produto.setQtde(novaQtde);
            produto.setPrecoCusto(novoCustoMedio);

            /* 🔵 CONTA A PAGAR */

            contaPagarService.criarContaPagarProduto(
                    fazenda,
                    safra,
                    "Fornecedor não informado",
                    dto.getNumeroNotaFiscal(),
                    total,
                    dto.getDataPagamento()
            );

        } else {

            if (estoqueAtual.compareTo(dto.getQtde()) < 0) {
                throw new AuthException("Estoque insuficiente");
            }

            produto.setQtde(estoqueAtual.subtract(dto.getQtde()));
        }

        produtoRepository.save(produto);

        return toResponseDTO(mov);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private MovProdutoResponseDTO toResponseDTO(MovProduto mov) {

        return MovProdutoResponseDTO.builder()
                .id(mov.getId())
                .produtoId(mov.getProduto() != null ? mov.getProduto().getId() : null)
                .produtoNome(mov.getProduto() != null ? mov.getProduto().getNome() : null)
                .fazendaId(mov.getFazenda() != null ? mov.getFazenda().getId() : null)
                .safraId(mov.getSafra() != null ? mov.getSafra().getId() : null)
                .tipoMovimentoId(mov.getTipoMovimento() != null ? mov.getTipoMovimento().getId() : null)
                .tipoMovimentoDescricao(mov.getTipoMovimento() != null ? mov.getTipoMovimento().getDescricao() : null)
                .dataMovimento(mov.getDataMovimento())
                .qtde(mov.getQtde())
                .vlrUnitario(mov.getVlrUnitario())
                .vlrTotal(mov.getVlrTotal())
                .numeroNotaFiscal(mov.getNumeroNotaFiscal())
                .numeroOrdemServico(mov.getNumeroOrdemServico())
                .dataPagamento(mov.getDataPagamento())
                .build();
    }
}