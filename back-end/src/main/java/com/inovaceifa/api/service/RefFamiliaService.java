package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefFamilia;
import com.inovaceifa.api.repository.RefFamiliaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefFamiliaService extends ReferenciaBaseService<RefFamilia> {

    private final RefFamiliaRepository repository;

    @Override
    protected JpaRepository<RefFamilia, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "familia";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefFamilia entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefFamilia createEntity(ReferenciaCreateDTO dto) {
        RefFamilia f = new RefFamilia();
        f.setDescricao(dto.getDescricao());
        f.setAtivo(true);
        return f;
    }

    @Override
    protected void updateEntity(RefFamilia entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}