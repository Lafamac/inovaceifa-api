package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Talhao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TalhaoRepository extends JpaRepository<Talhao, Long> {

    List<Talhao> findByFazendaId(Long fazendaId);

    Page<Talhao> findByFazendaId(Long fazendaId, Pageable pageable);

    Page<Talhao> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);

    Page<Talhao> findByFazendaIdAndAtivoFalse(Long fazendaId, Pageable pageable);


}