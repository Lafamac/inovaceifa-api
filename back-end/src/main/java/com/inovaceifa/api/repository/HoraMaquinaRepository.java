package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.HoraMaquina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HoraMaquinaRepository extends JpaRepository<HoraMaquina, Long> {

    List<HoraMaquina> findByFazendaId(Long fazendaId);

    List<HoraMaquina> findByMaquinaId(Long maquinaId);

    List<HoraMaquina> findBySafraId(Long safraId);

    List<HoraMaquina> findByOperacaoTalhao_Id(Long operacaoTalhaoId);

    Page<HoraMaquina> findByFazendaIdAndSafraId(Long fazendaId, Long safraId, Pageable pageable);

    List<HoraMaquina> findByFazendaIdAndSafraId(Long fazendaId, Long safraId);

    Page<HoraMaquina> findByFazendaId(Long fazendaId, Pageable pageable);

    Optional<HoraMaquina> findByIdAndFazendaId(Long id, Long fazendaId);

    List<HoraMaquina> findByOperacaoTalhao_OrdemServico_Id(Long ordemServicoId);

    /* 🔥 NOVO: SOMA POR SAFRA */

    @Query("""
        SELECT COALESCE(SUM(h.horasTrabalhadas * h.custoHora), 0)
        FROM HoraMaquina h
        WHERE h.safra.id = :safraId
    """)
    BigDecimal sumBySafra(Long safraId);

    @Query("""
        SELECT COALESCE(SUM(h.horasTrabalhadas * h.custoHora), 0)
        FROM HoraMaquina h
        WHERE h.operacaoTalhao.id = :operacaoId
    """)
    BigDecimal sumByOperacao(Long operacaoId);
}