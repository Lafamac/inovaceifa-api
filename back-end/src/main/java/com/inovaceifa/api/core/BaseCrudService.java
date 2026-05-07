package com.inovaceifa.api.core;

import com.inovaceifa.api.exception.AuthException;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseCrudService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    /* =========================================================
       BUSCAR ENTITY
       ========================================================= */

    protected T buscarEntity(ID id) {

        T entity = getRepository().findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        validarAcesso(entity);

        return entity;
    }

    /* =========================================================
       SALVAR ENTITY
       ========================================================= */

    protected T salvarEntity(T entity) {
        return getRepository().save(entity);
    }

    /* =========================================================
       HOOK DE SEGURANÇA
       ========================================================= */

    protected void validarAcesso(T entity) {
        // por padrão não faz nada
    }

}