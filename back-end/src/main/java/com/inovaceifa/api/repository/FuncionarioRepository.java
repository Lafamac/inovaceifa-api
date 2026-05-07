package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByUsuarioId(Long usuarioId);

    /* =========================================================
       🆕 CONTEXTO POR PROPRIETÁRIO
       ========================================================= */

    Page<Funcionario> findByProprietarioId(Long proprietarioId, Pageable pageable);

    List<Funcionario> findByProprietarioId(Long proprietarioId);

    Page<Funcionario> findByProprietarioIdAndAtivoFalse(Long proprietarioId, Pageable pageable);

    Optional<Funcionario> findByIdAndProprietarioId(Long id, Long proprietarioId);

    /* =========================================================
       🔵 LEGADO (mantido)
       ========================================================= */

    Page<Funcionario> findByFazendaId(Long fazendaId, Pageable pageable);

    List<Funcionario> findByFazendaId(Long fazendaId);

    Page<Funcionario> findByFazendaIdAndAtivoFalse(Long fazendaId, Pageable pageable);

    boolean existsByCpf(String cpf);
}