package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefTipoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RefTipoMaquinaRepository extends JpaRepository<RefTipoMaquina, Long> {

    List<RefTipoMaquina> findAllByOrderByDescricaoAsc();

}