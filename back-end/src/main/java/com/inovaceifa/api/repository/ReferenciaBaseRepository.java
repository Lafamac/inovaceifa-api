package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.ReferenciaBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ReferenciaBaseRepository<T extends ReferenciaBase>
        extends JpaRepository<T, Long> {

    List<T> findByAtivoTrueOrderByDescricaoAsc();

    List<T> findByAtivoFalseOrderByDescricaoAsc();

    boolean existsByDescricaoIgnoreCase(String descricao);
}