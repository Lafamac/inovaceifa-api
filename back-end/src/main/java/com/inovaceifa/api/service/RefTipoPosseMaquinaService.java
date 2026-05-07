package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefTipoPosseMaquina;
import com.inovaceifa.api.repository.RefTipoPosseMaquinaRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoPosseMaquinaService extends ReferenciaBaseService<RefTipoPosseMaquina> {

    private final RefTipoPosseMaquinaRepository repository;

    @Override
    protected JpaRepository<RefTipoPosseMaquina, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-posse-maquina";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoPosseMaquina entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoPosseMaquina createEntity(ReferenciaCreateDTO dto) {
        RefTipoPosseMaquina e = new RefTipoPosseMaquina();
        e.setDescricao(dto.getDescricao());
        e.setAtivo(true);
        return e;
    }

    @Override
    protected void updateEntity(RefTipoPosseMaquina entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}