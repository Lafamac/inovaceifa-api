package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefStCultivo;
import com.inovaceifa.api.repository.RefStCultivoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefStCultivoService extends ReferenciaBaseService<RefStCultivo> {

    private final RefStCultivoRepository repository;

    @Override
    protected JpaRepository<RefStCultivo, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "st-cultivo";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefStCultivo entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefStCultivo createEntity(ReferenciaCreateDTO dto) {
        RefStCultivo s = new RefStCultivo();
        s.setDescricao(dto.getDescricao());
        s.setAtivo(true);
        return s;
    }

    @Override
    protected void updateEntity(RefStCultivo entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}