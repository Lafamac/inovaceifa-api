package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.SegmentacaoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SegmentacaoFuncionarioRepository
        extends JpaRepository<SegmentacaoFuncionario, Long> {

    List<SegmentacaoFuncionario> findByFuncionario_IdAndSafra_IdAndAtivoTrue(
            Long funcionarioId,
            Long safraId
    );
}