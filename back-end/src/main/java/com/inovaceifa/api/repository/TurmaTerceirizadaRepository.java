package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.TurmaTerceirizada;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface TurmaTerceirizadaRepository extends JpaRepository<TurmaTerceirizada, Long> {

    Page<TurmaTerceirizada> findByProprietario_IdAndAtivoTrue(Long proprietarioId, Pageable pageable);

    Page<TurmaTerceirizada> findByProprietario_IdAndAtivoFalse(Long proprietarioId, Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(t.valorDiaria * t.quantidadePessoas), 0)
    FROM TurmaTerceirizada t
    WHERE t.operacaoTalhao.id = :operacaoId
""")
    BigDecimal sumByOperacao(Long operacaoId);
}