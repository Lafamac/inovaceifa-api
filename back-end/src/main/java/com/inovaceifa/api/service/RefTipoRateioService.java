package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefTipoRateio;
import com.inovaceifa.api.repository.RefTipoRateioRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoRateioService extends ReferenciaBaseService<RefTipoRateio> {

    private final RefTipoRateioRepository repository;

    @Override
    protected JpaRepository<RefTipoRateio, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-rateio";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoRateio entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoRateio createEntity(ReferenciaCreateDTO dto) {

        RefTipoRateio entity = new RefTipoRateio();

        entity.setDescricao(dto.getDescricao());

        return entity;
    }

    @Override
    protected void updateEntity(RefTipoRateio entity, ReferenciaUpdateDTO dto) {

        entity.setDescricao(dto.getDescricao());
    }
}