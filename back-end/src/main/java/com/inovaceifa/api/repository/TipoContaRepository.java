package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.TipoConta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoContaRepository extends JpaRepository<TipoConta, Long> {

    Page<TipoConta> findByAtivoTrue(Pageable pageable);

    Page<TipoConta> findByAtivoFalse(Pageable pageable);

    Page<TipoConta> findByArvoreContainingIgnoreCase(String arvore, Pageable pageable);

    Page<TipoConta> findByIndiceContainingIgnoreCase(String indice, Pageable pageable);

}