package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.PlanejamentoInsumoCreateDTO;
import com.inovaceifa.api.dto.planejamento.PlanejamentoInsumoResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanejamentoInsumoService {

    private final PlanejamentoOperacaoRepository planejamentoOperacaoRepository;
    private final PlanejamentoInsumoRepository repository;
    private final ProdutoRepository produtoRepository;

    // 🔥 NOVO
    private final PlanejamentoCalculoService calculoService;

    /* ========================= ADICIONAR INSUMO ========================= */

    public PlanejamentoInsumoResponseDTO adicionar(Long planejamentoId, PlanejamentoInsumoCreateDTO dto) {

        PlanejamentoOperacao planejamento = planejamentoOperacaoRepository.findById(planejamentoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

        if ("EXECUTADO".equals(planejamento.getStatus())) {
            throw new AuthException("Planejamento já executado");
        }

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new AuthException("Produto não encontrado"));

        PlanejamentoInsumo p = new PlanejamentoInsumo();

        p.setPlanejamentoOperacao(planejamento);
        p.setProduto(produto);
        p.setDosePorHa(dto.getDosePorHa());

        /* 🔥 NOVA REGRA: PREÇO AUTOMÁTICO */

        BigDecimal valorUnitario = dto.getValorUnitarioPrevisto() != null
                ? dto.getValorUnitarioPrevisto()
                : produto.getPrecoCusto();

        if (valorUnitario == null) {
            valorUnitario = BigDecimal.ZERO;
        }

        p.setValorUnitarioPrevisto(valorUnitario);

        /* 🔥 CÁLCULO AUTOMÁTICO */

        BigDecimal area = planejamento.getAreaPlanejada();

        BigDecimal dose = dto.getDosePorHa() != null ? dto.getDosePorHa() : BigDecimal.ZERO;

        BigDecimal quantidadeTotal = dose.multiply(area);

        p.setQuantidadeTotal(quantidadeTotal);

        BigDecimal valorTotal = quantidadeTotal.multiply(valorUnitario);
        p.setValorTotalPrevisto(valorTotal);

        p.setAtivo(true);

        p = repository.save(p);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);

        return toResponseDTO(p);
    }

    /* ========================= LISTAR ========================= */

    public List<PlanejamentoInsumoResponseDTO> listar(Long planejamentoId) {

        return repository.findByPlanejamentoOperacaoIdAndAtivoTrue(planejamentoId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* ========================= REMOVER ========================= */

    public void removerInsumo(Long planejamentoId, Long itemId) {

        PlanejamentoInsumo entity = repository.findById(itemId)
                .orElseThrow(() -> new AuthException("Insumo não encontrado"));

        if (!entity.getPlanejamentoOperacao().getId().equals(planejamentoId)) {
            throw new AuthException("Insumo não pertence ao planejamento");
        }

        entity.setAtivo(false);

        repository.save(entity);

        // 🔥 RECALCULA PLANEJAMENTO
        calculoService.recalcularEAtualizar(planejamentoId);
    }

    /* ========================= DTO ========================= */

    private PlanejamentoInsumoResponseDTO toResponseDTO(PlanejamentoInsumo p) {

        return PlanejamentoInsumoResponseDTO.builder()
                .id(p.getId())
                .planejamentoOperacaoId(p.getPlanejamentoOperacao().getId())
                .produtoId(p.getProduto().getId())
                .dosePorHa(p.getDosePorHa())
                .quantidadeTotal(p.getQuantidadeTotal())
                .valorUnitarioPrevisto(p.getValorUnitarioPrevisto())
                .valorTotalPrevisto(p.getValorTotalPrevisto())
                .ativo(p.getAtivo())
                .build();
    }
}