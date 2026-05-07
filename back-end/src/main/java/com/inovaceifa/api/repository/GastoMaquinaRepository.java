package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.GastoMaquina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GastoMaquinaRepository extends JpaRepository<GastoMaquina, Long> {

    Page<GastoMaquina> findByFazendaIdAndSafraId(Long fazendaId, Long safraId, Pageable pageable);

    List<GastoMaquina> findByFazendaIdAndSafraId(Long fazendaId, Long safraId);

}
