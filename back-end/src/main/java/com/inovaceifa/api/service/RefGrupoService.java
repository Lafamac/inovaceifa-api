package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefGrupo;
import com.inovaceifa.api.repository.RefGrupoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefGrupoService extends ReferenciaBaseService<RefGrupo> {

    private final RefGrupoRepository repository;

    @Override
    protected JpaRepository<RefGrupo, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "grupo";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefGrupo entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefGrupo createEntity(ReferenciaCreateDTO dto) {
        RefGrupo g = new RefGrupo();
        g.setDescricao(dto.getDescricao());
        g.setAtivo(true);
        return g;
    }

    @Override
    protected void updateEntity(RefGrupo entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}