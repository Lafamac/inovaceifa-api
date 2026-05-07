package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefUsuario;
import com.inovaceifa.api.repository.RefUsuarioRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefUsuarioService extends ReferenciaBaseService<RefUsuario> {

    private final RefUsuarioRepository repository;

    @Override
    protected JpaRepository<RefUsuario, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "usuario";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefUsuario entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefUsuario createEntity(ReferenciaCreateDTO dto) {
        RefUsuario u = new RefUsuario();
        u.setDescricao(dto.getDescricao());
        u.setAtivo(true);
        return u;
    }

    @Override
    protected void updateEntity(RefUsuario entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}