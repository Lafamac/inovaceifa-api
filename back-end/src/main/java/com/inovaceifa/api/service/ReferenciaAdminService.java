package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.service.referencia.ReferenciaRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReferenciaAdminService {

    private final ReferenciaRegistry registry;

    /* =========================================================
       LISTAR TODAS AS REFERÊNCIAS
    ========================================================= */

    public Map<String, List<ReferenciaResponseDTO>> listarTodas() {

        Map<String, List<ReferenciaResponseDTO>> result = new HashMap<>();

        registry.getAll().forEach((tipo, service) -> {
            result.put(tipo, service.listarAtivos());
        });

        return result;
    }

    /* =========================================================
       LISTAR ATIVOS
    ========================================================= */

    public List<ReferenciaResponseDTO> listar(String tipo) {
        return registry.getService(tipo).listarAtivos();
    }

    /* =========================================================
       LISTAR INATIVOS
    ========================================================= */

    public List<ReferenciaResponseDTO> listarInativos(String tipo) {
        return registry.getService(tipo).listarInativos();
    }

    /* =========================================================
       BUSCAR
    ========================================================= */

    public ReferenciaResponseDTO buscar(String tipo, Long id) {
        return registry.getService(tipo).buscar(id);
    }

    /* =========================================================
       CRIAR
    ========================================================= */

    public ReferenciaResponseDTO criar(String tipo, ReferenciaCreateDTO dto) {
        return registry.getService(tipo).criar(dto);
    }

    /* =========================================================
       ATUALIZAR
    ========================================================= */

    public ReferenciaResponseDTO atualizar(String tipo, Long id, ReferenciaUpdateDTO dto) {
        return registry.getService(tipo).atualizar(id, dto);
    }

    /* =========================================================
       DESATIVAR
    ========================================================= */

    public void desativar(String tipo, Long id) {
        registry.getService(tipo).desativar(id);
    }

    /* =========================================================
       REATIVAR
    ========================================================= */

    public void reativar(String tipo, Long id) {
        registry.getService(tipo).reativar(id);
    }

}