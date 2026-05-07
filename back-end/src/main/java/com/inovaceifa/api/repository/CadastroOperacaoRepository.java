package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.CadastroOperacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CadastroOperacaoRepository extends JpaRepository<CadastroOperacao, Long> {

    Page<CadastroOperacao> findAllByAtivoTrue(Pageable pageable);

    Page<CadastroOperacao> findAllByAtivoFalse(Pageable pageable);

}