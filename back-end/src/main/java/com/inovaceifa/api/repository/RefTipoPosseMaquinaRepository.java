package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoPosseMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefTipoPosseMaquinaRepository extends JpaRepository<RefTipoPosseMaquina, Long> {

    List<RefTipoPosseMaquina> findAllByOrderByDescricaoAsc();

}