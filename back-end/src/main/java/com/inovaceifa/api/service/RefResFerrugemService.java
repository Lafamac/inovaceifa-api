package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefResFerrugem;
import com.inovaceifa.api.repository.RefResFerrugemRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefResFerrugemService extends ReferenciaBaseService<RefResFerrugem> {

    private final RefResFerrugemRepository repository;

    @Override
    protected JpaRepository<RefResFerrugem, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "res-ferrugem";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefResFerrugem entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefResFerrugem createEntity(ReferenciaCreateDTO dto) {
        RefResFerrugem r = new RefResFerrugem();
        r.setDescricao(dto.getDescricao());
        r.setAtivo(true);
        return r;
    }

    @Override
    protected void updateEntity(RefResFerrugem entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}