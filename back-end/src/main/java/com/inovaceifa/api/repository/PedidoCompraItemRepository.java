package com.inovaceifa.api.repository;

import com.inovaceifa.api.model.PedidoCompraItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoCompraItemRepository extends JpaRepository<PedidoCompraItem, Long> {

    List<PedidoCompraItem> findByPedidoCompra_Id(Long pedidoCompraId);
}