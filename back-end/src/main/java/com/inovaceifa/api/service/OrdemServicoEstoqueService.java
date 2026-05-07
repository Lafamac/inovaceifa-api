package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.produto.MovProdutoCreateDTO;
import com.inovaceifa.api.dto.produto.MovProdutoRequestDTO;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoEstoqueService {

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final OperacaoCombustivelRepository operacaoCombustivelRepository;
    private final MovProdutoService movProdutoService;

    public void gerarConsumoDiesel(OrdemServico ordem, BigDecimal precoDiesel) {

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordem.getId());

        for (OperacaoTalhao talhao : talhoes) {

            List<OperacaoCombustivel> combustiveis =
                    operacaoCombustivelRepository.findByOperacaoTalhao_Id(talhao.getId());

            for (OperacaoCombustivel c : combustiveis) {

                if (c.getLitros() == null) continue;

                MovProdutoRequestDTO mov = new MovProdutoRequestDTO();

                mov.setProdutoId(1L); // diesel
                mov.setTipoMovimentoId(2L); // CONSUMO

                mov.setDataMovimento(LocalDate.now());

                mov.setQtde(c.getLitros());

                mov.setVlrUnitario(precoDiesel);

                mov.setNumeroOrdemServico(String.valueOf(ordem.getId()));

                movProdutoService.criar(mov);
            }
        }
    }
}