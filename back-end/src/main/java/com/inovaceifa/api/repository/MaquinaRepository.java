package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Maquina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MaquinaRepository extends JpaRepository<Maquina, Long> {

    /* =========================================================
       🔵 CONTEXTO NOVO — PROPRIETÁRIO
       ========================================================= */

    Page<Maquina> findByFazenda_Proprietario_IdAndAtivoTrue(Long proprietarioId, Pageable pageable);

    Page<Maquina> findByFazenda_Proprietario_IdAndAtivoFalse(Long proprietarioId, Pageable pageable);

    Optional<Maquina> findByIdAndFazenda_Proprietario_Id(Long id, Long proprietarioId);


    /* =========================================================
       🟡 LEGADO (mantido para compatibilidade)
       ========================================================= */

    Page<Maquina> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);

    Page<Maquina> findByFazendaIdAndAtivoFalse(Long fazendaId, Pageable pageable);

    Optional<Maquina> findByIdAndFazendaId(Long id, Long fazendaId);
}