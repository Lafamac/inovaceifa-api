package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Fazenda;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FazendaRepository extends JpaRepository<Fazenda, Long> {

    // MÉTODOS ORIGINAIS (NÃO ALTERAR)
    Page<Fazenda> findByProprietarioId(Long proprietarioId, Pageable pageable);

    List<Fazenda> findByProprietarioId(Long proprietarioId);

    boolean existsByCnpj(String cnpj);

    // NOVOS MÉTODOS (APENAS ADICIONADOS)
    Page<Fazenda> findByProprietarioIdAndAtivoTrue(Long proprietarioId, Pageable pageable);

    List<Fazenda> findByProprietarioIdAndAtivoTrue(Long proprietarioId);

    Page<Fazenda> findByProprietarioIdAndAtivoFalse(Long proprietarioId, Pageable pageable);
}