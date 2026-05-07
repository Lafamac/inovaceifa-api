package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefDespesa;
import com.inovaceifa.api.repository.RefDespesaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefDespesaService extends ReferenciaBaseService<RefDespesa> {

    private final RefDespesaRepository repository;

    @Override
    protected JpaRepository<RefDespesa, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "despesa";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefDespesa entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefDespesa createEntity(ReferenciaCreateDTO dto) {
        RefDespesa d = new RefDespesa();
        d.setDescricao(dto.getDescricao());
        d.setAtivo(true);
        return d;
    }

    @Override
    protected void updateEntity(RefDespesa entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}