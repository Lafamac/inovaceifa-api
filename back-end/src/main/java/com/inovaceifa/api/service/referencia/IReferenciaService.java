package com.inovaceifa.api.service.referencia;

import com.inovaceifa.api.dto.referencia.*;

import java.util.List;

public interface IReferenciaService {

    String getTipo();

    List<ReferenciaResponseDTO> listarAtivos();

    List<ReferenciaResponseDTO> listarInativos();

    ReferenciaResponseDTO buscar(Long id);

    ReferenciaResponseDTO criar(ReferenciaCreateDTO dto);

    ReferenciaResponseDTO atualizar(Long id, ReferenciaUpdateDTO dto);

    void desativar(Long id);

    void reativar(Long id);

}