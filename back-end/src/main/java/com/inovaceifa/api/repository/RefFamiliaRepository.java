package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefFamilia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefFamiliaRepository extends JpaRepository<RefFamilia, Long> {

    List<RefFamilia> findAllByOrderByDescricaoAsc();

}