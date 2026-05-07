package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefCentroCusto;
import com.inovaceifa.api.repository.RefCentroCustoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefCentroCustoService extends ReferenciaBaseService<RefCentroCusto> {

    private final RefCentroCustoRepository repository;

    @Override
    protected JpaRepository<RefCentroCusto, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "centro-custo";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefCentroCusto entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefCentroCusto createEntity(ReferenciaCreateDTO dto) {

        RefCentroCusto c = new RefCentroCusto();
        c.setDescricao(dto.getDescricao());
        c.setAtivo(true);

        return c;
    }

    @Override
    protected void updateEntity(RefCentroCusto entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}