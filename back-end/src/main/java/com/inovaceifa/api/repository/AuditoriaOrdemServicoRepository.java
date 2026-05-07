package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.AuditoriaOrdemServico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaOrdemServicoRepository extends JpaRepository<AuditoriaOrdemServico, Long> {

    List<AuditoriaOrdemServico> findByOrdemServicoIdOrderByDataEventoDesc(Long ordemServicoId);
}