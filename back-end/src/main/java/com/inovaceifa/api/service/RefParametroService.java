package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefParametro;
import com.inovaceifa.api.repository.RefParametroRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefParametroService extends ReferenciaBaseService<RefParametro> {

    private final RefParametroRepository repository;

    @Override
    protected JpaRepository<RefParametro, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "parametro";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefParametro entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build() // ⚠️ IMPORTANTE
                .extra("chave", entity.getChave())
                .extra("valor", entity.getValor());
    }

    @Override
    protected RefParametro createEntity(ReferenciaCreateDTO dto) {

        RefParametro p = new RefParametro();

        p.setDescricao(dto.getDescricao());
        p.setChave(dto.getChave());
        p.setValor(dto.getValor());
        p.setAtivo(true);

        return p;
    }

    @Override
    protected void updateEntity(RefParametro entity, ReferenciaUpdateDTO dto) {

        entity.setDescricao(dto.getDescricao());

        if (dto.getChave() != null) {
            entity.setChave(dto.getChave());
        }

        if (dto.getValor() != null) {
            entity.setValor(dto.getValor());
        }
    }
}