package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.lancamento.LancamentoCreateDTO;
import com.inovaceifa.api.model.OrdemServico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OrdemServicoFinanceiroService {

    private final LancamentoDespesaService lancamentoService;

    public void gerarLancamentoDespesa(OrdemServico ordem, BigDecimal custoTotal) {

        LancamentoCreateDTO lanc = new LancamentoCreateDTO();

        lanc.setRefDespesaId(1L);

        lanc.setCentroCustoId(1L);

        lanc.setValor(custoTotal);

        lanc.setData(LocalDate.now());

        lanc.setObservacao(
                "Despesa gerada automaticamente pela Ordem de Serviço #" + ordem.getId()
        );

        lancamentoService.criar(lanc);
    }
}