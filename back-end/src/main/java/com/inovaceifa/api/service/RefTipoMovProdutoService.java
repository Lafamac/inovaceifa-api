package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefTipoMovProduto;
import com.inovaceifa.api.repository.RefTipoMovProdutoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoMovProdutoService extends ReferenciaBaseService<RefTipoMovProduto> {

    private final RefTipoMovProdutoRepository repository;

    @Override
    protected JpaRepository<RefTipoMovProduto, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-mov-produto";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoMovProduto entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoMovProduto createEntity(ReferenciaCreateDTO dto) {
        RefTipoMovProduto t = new RefTipoMovProduto();
        t.setDescricao(dto.getDescricao());
        t.setAtivo(true);
        return t;
    }

    @Override
    protected void updateEntity(RefTipoMovProduto entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}