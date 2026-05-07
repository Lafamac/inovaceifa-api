package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.RefPedidoCompraStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefPedidoCompraStatusRepository
        extends JpaRepository<RefPedidoCompraStatus, Long> {

    Optional<RefPedidoCompraStatus> findByDescricaoIgnoreCase(String descricao);
}