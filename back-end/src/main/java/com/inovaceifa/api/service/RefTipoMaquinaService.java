package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefTipoMaquina;
import com.inovaceifa.api.repository.RefTipoMaquinaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoMaquinaService extends ReferenciaBaseService<RefTipoMaquina> {

    private final RefTipoMaquinaRepository repository;

    @Override
    protected JpaRepository<RefTipoMaquina, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-maquina";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoMaquina entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoMaquina createEntity(ReferenciaCreateDTO dto) {
        RefTipoMaquina t = new RefTipoMaquina();
        t.setDescricao(dto.getDescricao());
        t.setAtivo(true);
        return t;
    }

    @Override
    protected void updateEntity(RefTipoMaquina entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}