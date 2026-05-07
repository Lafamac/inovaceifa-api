package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoRateio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefTipoRateioRepository extends JpaRepository<RefTipoRateio, Long> {

    List<RefTipoRateio> findAllByAtivoTrueOrderByDescricaoAsc();

}