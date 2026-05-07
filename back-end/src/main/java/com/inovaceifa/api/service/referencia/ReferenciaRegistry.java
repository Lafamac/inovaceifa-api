package com.inovaceifa.api.service.referencia;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReferenciaRegistry {

    private final List<IReferenciaService> services;

    public IReferenciaService getService(String tipo) {

        return services.stream()
                .filter(s -> s.getTipo().equalsIgnoreCase(tipo))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Tipo de referência não encontrado: " + tipo));
    }

    public Map<String, IReferenciaService> getAll() {

        return services.stream()
                .collect(Collectors.toMap(
                        IReferenciaService::getTipo,
                        s -> s
                ));
    }

}