package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.model.RefPedidoCompraStatus;
import com.inovaceifa.api.repository.RefPedidoCompraStatusRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefPedidoCompraStatusService
        extends ReferenciaBaseService<RefPedidoCompraStatus> {

    private final RefPedidoCompraStatusRepository repository;

    @Override
    protected JpaRepository<RefPedidoCompraStatus, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "pedido-compra-status";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefPedidoCompraStatus entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefPedidoCompraStatus createEntity(ReferenciaCreateDTO dto) {
        RefPedidoCompraStatus entity = new RefPedidoCompraStatus();
        entity.setDescricao(dto.getDescricao());
        return entity;
    }

    @Override
    protected void updateEntity(RefPedidoCompraStatus entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}