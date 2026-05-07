package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.VendaProducao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface VendaProducaoRepository extends JpaRepository<VendaProducao, Long> {

    List<VendaProducao> findBySafraTalhao_Id(Long safraTalhaoId);

    @Query("""
        SELECT COALESCE(SUM(v.valorTotal), 0)
        FROM VendaProducao v
        WHERE v.safraTalhao.id = :safraTalhaoId
    """)
    BigDecimal sumValorBySafraTalhao(Long safraTalhaoId);

    @Query("""
        SELECT COALESCE(SUM(v.quantidade), 0)
        FROM VendaProducao v
        WHERE v.safraTalhao.id = :safraTalhaoId
    """)
    BigDecimal sumQuantidadeBySafraTalhao(Long safraTalhaoId);
}