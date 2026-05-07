package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefDespesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefDespesaRepository extends JpaRepository<RefDespesa, Long> {

    List<RefDespesa> findAllByOrderByDescricaoAsc();

}