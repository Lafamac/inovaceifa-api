package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoMovProduto;
import com.inovaceifa.api.model.RefTipoPagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefTipoPagamentoRepository
        extends JpaRepository<RefTipoPagamento, Long> {
    List<RefTipoPagamento> findAllByOrderByDescricaoAsc();
}