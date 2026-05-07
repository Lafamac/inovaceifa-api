package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefOperacaoTalhao;
import com.inovaceifa.api.repository.RefOperacaoTalhaoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefOperacaoTalhaoService
        extends ReferenciaBaseService<RefOperacaoTalhao> {

    private final RefOperacaoTalhaoRepository repository;

    @Override
    protected JpaRepository<RefOperacaoTalhao, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "operacao-talhao";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefOperacaoTalhao entity) {

        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefOperacaoTalhao createEntity(ReferenciaCreateDTO dto) {

        RefOperacaoTalhao entity = new RefOperacaoTalhao();

        entity.setDescricao(dto.getDescricao());

        return entity;
    }

    @Override
    protected void updateEntity(
            RefOperacaoTalhao entity,
            ReferenciaUpdateDTO dto
    ) {

        entity.setDescricao(dto.getDescricao());
    }
}