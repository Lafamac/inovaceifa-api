package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoGastoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RefTipoGastoMaquinaRepository extends JpaRepository<RefTipoGastoMaquina, Long> {

    List<RefTipoGastoMaquina> findAllByOrderByDescricaoAsc();

}