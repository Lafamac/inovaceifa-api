package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Terceirizado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TerceirizadoRepository extends JpaRepository<Terceirizado, Long> {

    Page<Terceirizado> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);

    Page<Terceirizado> findByFazendaIdAndAtivoFalse(Long fazendaId, Pageable pageable);

    boolean existsByCpf(String cpf);
}