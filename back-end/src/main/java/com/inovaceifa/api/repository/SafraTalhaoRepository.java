package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.SafraTalhao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface SafraTalhaoRepository extends JpaRepository<SafraTalhao, Long> {

    List<SafraTalhao> findBySafraId(Long safraId);

    Optional<SafraTalhao> findBySafraIdAndTalhaoId(Long safraId, Long talhaoId);

    Page<SafraTalhao> findBySafraId(Long safraId, Pageable pageable);

    // 🔥 NOVOS
    Page<SafraTalhao> findBySafraIdAndAtivoTrue(Long safraId, Pageable pageable);

    Page<SafraTalhao> findBySafraIdAndAtivoFalse(Long safraId, Pageable pageable);

    @Query("""
    SELECT COALESCE(SUM(s.areaUtilizada), 0)
    FROM SafraTalhao s
    WHERE s.safra.id = :safraId
""")
    BigDecimal sumAreaBySafra(Long safraId);

    @Query("""
    SELECT COALESCE(SUM(s.producaoReal), 0)
    FROM SafraTalhao s
    WHERE s.safra.id = :safraId
""")
    BigDecimal sumProducaoBySafra(Long safraId);


    List<SafraTalhao> findBySafra_Id(Long safraId);
}