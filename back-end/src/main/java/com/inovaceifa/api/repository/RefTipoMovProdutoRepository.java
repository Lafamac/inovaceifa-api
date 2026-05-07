package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoMovProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefTipoMovProdutoRepository extends JpaRepository<RefTipoMovProduto, Long> {

    List<RefTipoMovProduto> findAllByOrderByDescricaoAsc();

}