package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.pedidocompra.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoCompraService {

    private final PedidoCompraRepository repository;
    private final PedidoCompraItemRepository itemRepository;
    private final ProdutoRepository produtoRepository;
    private final RefPedidoCompraStatusRepository statusRepository;

    private final ContextoFazendaService contexto;

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<PedidoCompraResponseDTO> listar(Pageable pageable) {

        Proprietario prop = contexto.getProprietario();
        Fazenda faz = contexto.getFazendaAtiva();
        Safra saf = contexto.getSafraAtiva();

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoTrue(
                        prop.getId(),
                        faz.getId(),
                        saf.getId(),
                        pageable
                ),
                this::toDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<PedidoCompraResponseDTO> listarInativos(Pageable pageable) {

        Proprietario prop = contexto.getProprietario();
        Fazenda faz = contexto.getFazendaAtiva();
        Safra saf = contexto.getSafraAtiva();

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoFalse(
                        prop.getId(),
                        faz.getId(),
                        saf.getId(),
                        pageable
                ),
                this::toDTO
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public PedidoCompraResponseDTO buscar(Long id) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        return toDTO(p);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Transactional
    public PedidoCompraResponseDTO criar(PedidoCompraCreateDTO dto) {

        PedidoCompra p = new PedidoCompra();

        p.setProprietario(contexto.getProprietario());
        p.setFazenda(contexto.getFazendaAtiva());
        p.setSafra(contexto.getSafraAtiva());
        p.setData(dto.getData() != null ? dto.getData() : LocalDate.now());

        RefPedidoCompraStatus status = statusRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new AuthException("Status não encontrado"));

        p.setStatus(status);
        p.setValorTotal(BigDecimal.ZERO);
        p.setAtivo(true);

        p = repository.save(p);

        salvarItens(p, dto.getItens());

        return toDTO(p);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Transactional
    public PedidoCompraResponseDTO atualizar(Long id, PedidoCompraUpdateDTO dto) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        validarEdicao(p);

        if (dto.getData() != null) {
            p.setData(dto.getData());
        }

        if (dto.getStatusId() != null) {
            RefPedidoCompraStatus status = statusRepository.findById(dto.getStatusId())
                    .orElseThrow(() -> new AuthException("Status não encontrado"));

            p.setStatus(status);
        }

        itemRepository.deleteAll(
                itemRepository.findByPedidoCompra_Id(p.getId())
        );

        salvarItens(p, dto.getItens());

        return toDTO(p);
    }

    /* =========================================================
       INATIVAR (SOFT DELETE)
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        validarEdicao(p);

        p.setAtivo(false);

        repository.save(p);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        p.setAtivo(true);

        repository.save(p);
    }

    /* =========================================================
       APROVAR
       ========================================================= */

    @Transactional
    public void aprovar(Long id) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        RefPedidoCompraStatus status = statusRepository.findById(2L)
                .orElseThrow(() -> new AuthException("Status aprovado não encontrado"));

        p.setStatus(status);

        repository.save(p);
    }

    /* =========================================================
       RECEBER
       ========================================================= */

    @Transactional
    public void receber(Long id) {

        PedidoCompra p = repository.findById(id)
                .orElseThrow(() -> new AuthException("Pedido não encontrado"));

        RefPedidoCompraStatus status = statusRepository.findById(3L)
                .orElseThrow(() -> new AuthException("Status recebido não encontrado"));

        p.setStatus(status);

        repository.save(p);
    }

    /* =========================================================
       SALVAR ITENS
       ========================================================= */

    private void salvarItens(PedidoCompra pedido, List<PedidoCompraItemDTO> itens) {

        BigDecimal total = BigDecimal.ZERO;

        for (PedidoCompraItemDTO dto : itens) {

            Produto produto = produtoRepository.findById(dto.getProdutoId())
                    .orElseThrow(() -> new AuthException("Produto não encontrado"));

            BigDecimal valorTotal = dto.getQuantidade()
                    .multiply(dto.getValorUnitario());

            PedidoCompraItem item = new PedidoCompraItem();

            item.setPedidoCompra(pedido);
            item.setProduto(produto);
            item.setQuantidade(dto.getQuantidade());
            item.setValorUnitario(dto.getValorUnitario());
            item.setValorTotal(valorTotal);

            itemRepository.save(item);

            total = total.add(valorTotal);
        }

        pedido.setValorTotal(total);

        repository.save(pedido);
    }

    /* =========================================================
       VALIDAÇÃO
       ========================================================= */

    private void validarEdicao(PedidoCompra p) {

        if (!p.getAtivo()) {
            throw new AuthException("Pedido inativo não pode ser alterado");
        }
    }

    /* =========================================================
       DTO
       ========================================================= */

    private PedidoCompraResponseDTO toDTO(PedidoCompra p) {

        List<PedidoCompraItem> itens =
                itemRepository.findByPedidoCompra_Id(p.getId());

        return PedidoCompraResponseDTO.builder()
                .id(p.getId())
                .data(p.getData())
                .statusId(p.getStatus().getId())
                .statusDescricao(p.getStatus().getDescricao())
                .valorTotal(p.getValorTotal())
                .ativo(p.getAtivo())
                .itens(
                        itens.stream().map(i ->
                                PedidoCompraItemResponseDTO.builder()
                                        .produtoId(i.getProduto().getId())
                                        .produtoNome(i.getProduto().getNome())
                                        .quantidade(i.getQuantidade())
                                        .valorUnitario(i.getValorUnitario())
                                        .valorTotal(i.getValorTotal())
                                        .build()
                        ).toList()
                )
                .build();
    }
}