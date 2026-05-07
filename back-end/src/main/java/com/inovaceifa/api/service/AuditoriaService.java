package com.inovaceifa.api.service;

import com.inovaceifa.api.model.AuditoriaOrdemServico;
import com.inovaceifa.api.repository.AuditoriaOrdemServicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final AuditoriaOrdemServicoRepository repository;

    public void registrar(
            Long ordemId,
            Long usuarioId,
            String acao,
            String antes,
            String depois
    ) {

        AuditoriaOrdemServico log = new AuditoriaOrdemServico();

        log.setOrdemServicoId(ordemId);
        log.setUsuarioId(usuarioId);
        log.setAcao(acao);
        log.setDadosAntes(antes);
        log.setDadosDepois(depois);
        log.setDataEvento(LocalDateTime.now());

        repository.save(log);
    }
}