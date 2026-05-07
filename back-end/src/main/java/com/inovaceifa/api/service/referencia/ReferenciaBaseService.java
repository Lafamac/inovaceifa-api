package com.inovaceifa.api.service.referencia;

import com.inovaceifa.api.dto.referencia.ReferenciaCreateDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaResponseDTO;
import com.inovaceifa.api.dto.referencia.ReferenciaUpdateDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.ReferenciaBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class ReferenciaBaseService<T extends ReferenciaBase>
        implements IReferenciaService{

    protected abstract JpaRepository<T, Long> getRepository();

    public abstract String getTipo();

    protected abstract ReferenciaResponseDTO toDTO(T entity);

    protected abstract T createEntity(ReferenciaCreateDTO dto);

    protected abstract void updateEntity(T entity, ReferenciaUpdateDTO dto);

    /* =========================================================
       LISTAR TODOS
       ========================================================= */

    public List<ReferenciaResponseDTO> listar() {

        return getRepository().findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public List<ReferenciaResponseDTO> listarAtivos() {

        return getRepository().findAll()
                .stream()
                .filter(ReferenciaBase::getAtivo)
                .map(this::toDTO)
                .toList();
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public List<ReferenciaResponseDTO> listarInativos() {

        return getRepository().findAll()
                .stream()
                .filter(e -> !e.getAtivo())
                .map(this::toDTO)
                .toList();
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public ReferenciaResponseDTO buscar(Long id) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        return toDTO(entity);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public ReferenciaResponseDTO criar(ReferenciaCreateDTO dto) {

        T entity = createEntity(dto);

        entity.setAtivo(true);

        return toDTO(getRepository().save(entity));
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public ReferenciaResponseDTO atualizar(Long id, ReferenciaUpdateDTO dto) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        updateEntity(entity, dto);

        return toDTO(getRepository().save(entity));
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    public void desativar(Long id) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        entity.setAtivo(false);

        getRepository().save(entity);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    public void reativar(Long id) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        entity.setAtivo(true);

        getRepository().save(entity);
    }
}