package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefCultura;
import com.inovaceifa.api.repository.RefCulturaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefCulturaService extends ReferenciaBaseService<RefCultura> {

    private final RefCulturaRepository repository;

    @Override
    protected JpaRepository<RefCultura, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "cultura";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefCultura entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefCultura createEntity(ReferenciaCreateDTO dto) {
        RefCultura c = new RefCultura();
        c.setDescricao(dto.getDescricao());
        c.setAtivo(true);
        return c;
    }

    @Override
    protected void updateEntity(RefCultura entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}