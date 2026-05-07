package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.OperacaoFuncionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface OperacaoFuncionarioRepository extends JpaRepository<OperacaoFuncionario, Long> {

    /* CONTEXTO */

    Page<OperacaoFuncionario> findByProprietario_IdAndFazenda_IdAndSafra_Id(
            Long proprietarioId,
            Long fazendaId,
            Long safraId,
            Pageable pageable
    );

    @Query("""
        SELECT COALESCE(SUM(
            of.horasTrabalhadas * 
            (f.salario / 220) * 
            (1 + :percentualEncargos)
        ), 0)
        FROM OperacaoFuncionario of
        JOIN of.funcionario f
        WHERE of.operacaoTalhao.id = :operacaoId
    """)
    BigDecimal sumByOperacao(Long operacaoId, BigDecimal percentualEncargos);

    List<OperacaoFuncionario> findByOperacaoTalhao_Id(Long operacaoTalhaoId);
}