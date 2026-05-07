package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.*;
import com.inovaceifa.api.model.RefTipoPagamento;
import com.inovaceifa.api.repository.RefTipoPagamentoRepository;
import com.inovaceifa.api.service.referencia.ReferenciaBaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefTipoPagamentoService extends ReferenciaBaseService<RefTipoPagamento> {

    private final RefTipoPagamentoRepository repository;

    @Override
    protected JpaRepository<RefTipoPagamento, Long> getRepository() {
        return repository;
    }

    @Override
    public String getTipo() {
        return "tipo-pagamento";
    }

    @Override
    protected ReferenciaResponseDTO toDTO(RefTipoPagamento entity) {
        return ReferenciaResponseDTO.builder()
                .id(entity.getId())
                .descricao(entity.getDescricao())
                .ativo(entity.getAtivo())
                .build();
    }

    @Override
    protected RefTipoPagamento createEntity(ReferenciaCreateDTO dto) {
        RefTipoPagamento e = new RefTipoPagamento();
        e.setDescricao(dto.getDescricao());
        e.setAtivo(true);
        return e;
    }

    @Override
    protected void updateEntity(RefTipoPagamento entity, ReferenciaUpdateDTO dto) {
        entity.setDescricao(dto.getDescricao());
    }
}