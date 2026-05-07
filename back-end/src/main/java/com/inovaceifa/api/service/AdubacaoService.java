package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.planejamento.AdubacaoResumoDTO;
import com.inovaceifa.api.dto.planejamento.AdubacaoTalhaoDTO;
import com.inovaceifa.api.dto.planejamento.AdubacaoResponseDTO;
import com.inovaceifa.api.model.PlanejamentoOperacao;
import com.inovaceifa.api.model.PlanejamentoInsumo;
import com.inovaceifa.api.repository.PlanejamentoOperacaoRepository;
import com.inovaceifa.api.repository.PlanejamentoInsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdubacaoService {

    private final PlanejamentoOperacaoRepository planejamentoRepository;
    private final PlanejamentoInsumoRepository insumoRepository;

    public AdubacaoResponseDTO gerar(Long safraId) {

        List<PlanejamentoOperacao> planejamentos =
                planejamentoRepository.findBySafraTalhao_Safra_IdAndAtivoTrue(safraId);

        Map<Long, AdubacaoTalhaoDTO> talhaoMap = new HashMap<>();
        Map<Long, BigDecimal> totalGeralMap = new HashMap<>();

        for (PlanejamentoOperacao p : planejamentos) {

            Long talhaoId = p.getSafraTalhao().getTalhao().getId();

            AdubacaoTalhaoDTO talhaoDTO = talhaoMap.computeIfAbsent(talhaoId, id ->
                    AdubacaoTalhaoDTO.builder()
                            .talhaoId(id)
                            .talhaoNome(p.getSafraTalhao().getTalhao().getNome())
                            .area(p.getAreaPlanejada())
                            .produtos(new ArrayList<>())
                            .build()
            );

            List<PlanejamentoInsumo> insumos =
                    insumoRepository.findByPlanejamentoOperacaoIdAndAtivoTrue(p.getId());

            Map<Long, BigDecimal> produtoMap = new HashMap<>();
            Map<Long, String> produtoNomeMap = new HashMap<>();

            for (PlanejamentoInsumo insumo : insumos) {

                Long produtoId = insumo.getProduto().getId();

                BigDecimal quantidade = insumo.getQuantidadeTotal() != null
                        ? insumo.getQuantidadeTotal()
                        : BigDecimal.ZERO;

                produtoMap.merge(produtoId, quantidade, BigDecimal::add);
                totalGeralMap.merge(produtoId, quantidade, BigDecimal::add);

                produtoNomeMap.put(produtoId, insumo.getProduto().getNome());
            }

            List<AdubacaoResumoDTO> produtos = produtoMap.entrySet().stream()
                    .map(e -> AdubacaoResumoDTO.builder()
                            .produtoId(e.getKey())
                            .produtoNome(produtoNomeMap.get(e.getKey()))
                            .quantidadeTotal(e.getValue())
                            .build())
                    .toList();

            talhaoDTO.getProdutos().addAll(produtos);
        }

        List<AdubacaoResumoDTO> totalGeral = totalGeralMap.entrySet().stream()
                .map(e -> AdubacaoResumoDTO.builder()
                        .produtoId(e.getKey())
                        .quantidadeTotal(e.getValue())
                        .build())
                .toList();

        return AdubacaoResponseDTO.builder()
                .safraId(safraId)
                .talhoes(new ArrayList<>(talhaoMap.values()))
                .totalGeral(totalGeral)
                .build();
    }
}