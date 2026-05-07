package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefCentroCusto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefCentroCustoRepository extends JpaRepository<RefCentroCusto, Long> {

    List<RefCentroCusto> findAllByAtivoTrueOrderByDescricaoAsc();

    List<RefCentroCusto> findAllByAtivoFalseOrderByDescricaoAsc();

}