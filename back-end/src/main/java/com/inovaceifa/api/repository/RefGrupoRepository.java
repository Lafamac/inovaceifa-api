package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefGrupo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefGrupoRepository extends JpaRepository<RefGrupo, Long> {

    List<RefGrupo> findAllByOrderByDescricaoAsc();

}