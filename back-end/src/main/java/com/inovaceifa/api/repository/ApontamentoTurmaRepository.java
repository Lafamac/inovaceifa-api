package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.ApontamentoTurma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApontamentoTurmaRepository
        extends JpaRepository<ApontamentoTurma, Long> {

    List<ApontamentoTurma> findByOrdemServicoId(Long ordemServicoId);

    List<ApontamentoTurma> findByFazendaIdAndSafraId(Long fazendaId, Long safraId);
}