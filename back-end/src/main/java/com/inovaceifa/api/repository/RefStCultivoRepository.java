package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefStCultivo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefStCultivoRepository extends JpaRepository<RefStCultivo, Long> {

    List<RefStCultivo> findAllByOrderByDescricaoAsc();

}