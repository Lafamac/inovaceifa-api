package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.MovProduto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovProdutoRepository extends JpaRepository<MovProduto, Long> {

    // LEGADO
    List<MovProduto> findByFazendaId(Long fazendaId);
    Page<MovProduto> findByFazendaId(Long fazendaId, Pageable pageable);

    // NOVO PADRÃO (fazenda + safra)
    List<MovProduto> findByFazendaIdAndSafraId(Long fazendaId, Long safraId);
    Page<MovProduto> findByFazendaIdAndSafraId(Long fazendaId, Long safraId, Pageable pageable);

    List<MovProduto> findByProdutoId(Long produtoId);
    List<MovProduto> findBySafraId(Long safraId);
}
