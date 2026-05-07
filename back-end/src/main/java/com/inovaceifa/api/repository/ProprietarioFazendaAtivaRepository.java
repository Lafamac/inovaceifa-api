package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.ProprietarioFazendaAtiva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProprietarioFazendaAtivaRepository
        extends JpaRepository<ProprietarioFazendaAtiva, Long> {

    Optional<ProprietarioFazendaAtiva> findByProprietarioId(Long proprietarioId);
}
