package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.ContaGerencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaGerencialRepository extends JpaRepository<ContaGerencial, Long> {

    List<ContaGerencial> findAllByOrderByDescricaoAsc();

}