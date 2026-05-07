package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Proprietario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {

    Optional<Proprietario> findByUsuario_Id(Long usuarioId);

    boolean existsByCpf(String cpf);

    boolean existsByEmail(String email);

    Page<Proprietario> findAll(Pageable pageable);

    // NOVOS
    Page<Proprietario> findByAtivoTrue(Pageable pageable);
    Page<Proprietario> findByAtivoFalse(Pageable pageable);
}