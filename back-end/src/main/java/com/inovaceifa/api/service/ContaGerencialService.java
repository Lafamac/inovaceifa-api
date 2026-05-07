package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.ContaGerencial;
import com.inovaceifa.api.repository.ContaGerencialRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContaGerencialService extends ReferenciaBaseService<ContaGerencial> {

    private final ContaGerencialRepository repository;

    @Override
    protected JpaRepository<ContaGerencial, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "conta-gerencial";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(ContaGerencial entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected ContaGerencial createEntity(ReferenciaCreateDTO dto) {
        ContaGerencial c = new ContaGerencial();
        c.setDescricao(dto.getDescricao());
        c.setAtivo(true);
        return c;
    }

    @Override
    protected void updateEntity(ContaGerencial entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}