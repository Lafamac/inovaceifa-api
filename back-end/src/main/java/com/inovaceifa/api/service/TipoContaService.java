package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.tipoconta.*;
import com.inovaceifa.api.model.TipoConta;
import com.inovaceifa.api.repository.TipoContaRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TipoContaService extends BaseCrudService<TipoConta, Long> {

    private final TipoContaRepository repository;

    @Override
    protected JpaRepository<TipoConta, Long> getRepository() {
        return repository;
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public PageResponseDTO<TipoContaResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findAll(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<TipoContaResponseDTO> listarAtivos(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByAtivoTrue(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<TipoContaResponseDTO> listarInativos(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByAtivoFalse(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       BUSCAR POR ÁRVORE
       ========================================================= */

    public PageResponseDTO<TipoContaResponseDTO> buscarPorArvore(String arvore, Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByArvoreContainingIgnoreCase(arvore, pageable),
                this::toDTO
        );
    }

    /* =========================================================
       BUSCAR POR ÍNDICE
       ========================================================= */

    public PageResponseDTO<TipoContaResponseDTO> buscarPorIndice(String indice, Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByIndiceContainingIgnoreCase(indice, pageable),
                this::toDTO
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public TipoContaResponseDTO criar(TipoContaCreateDTO dto) {

        TipoConta entity = new TipoConta();

        entity.setArvore(dto.getArvore());
        entity.setIndice(dto.getIndice());
        entity.setAtivo(true);

        entity = super.salvarEntity(entity);

        return toDTO(entity);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public TipoContaResponseDTO atualizar(Long id, TipoContaUpdateDTO dto) {

        TipoConta entity = super.buscarEntity(id);

        entity.setArvore(dto.getArvore());
        entity.setIndice(dto.getIndice());
        entity.setAtivo(dto.getAtivo());

        entity = super.salvarEntity(entity);

        return toDTO(entity);
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    public void desativar(Long id) {

        TipoConta entity = super.buscarEntity(id);

        entity.setAtivo(false);

        super.salvarEntity(entity);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private TipoContaResponseDTO toDTO(TipoConta entity) {

        return TipoContaResponseDTO.builder()
                .id(entity.getId())
                .arvore(entity.getArvore())
                .indice(entity.getIndice())
                .ativo(entity.getAtivo())
                .build();
    }
}