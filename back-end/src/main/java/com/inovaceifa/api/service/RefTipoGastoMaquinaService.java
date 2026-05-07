package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefTipoGastoMaquina;
import com.inovaceifa.api.repository.RefTipoGastoMaquinaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoGastoMaquinaService extends ReferenciaBaseService<RefTipoGastoMaquina> {

    private final RefTipoGastoMaquinaRepository repository;

    @Override
    protected JpaRepository<RefTipoGastoMaquina, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-gasto-maquina";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoGastoMaquina entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoGastoMaquina createEntity(ReferenciaCreateDTO dto) {
        RefTipoGastoMaquina t = new RefTipoGastoMaquina();
        t.setDescricao(dto.getDescricao());
        t.setAtivo(true);
        return t;
    }

    @Override
    protected void updateEntity(RefTipoGastoMaquina entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}