package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefParametro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefParametroRepository extends JpaRepository<RefParametro, Long> {

    Optional<RefParametro> findByChaveAndAtivoTrue(String chave);
}