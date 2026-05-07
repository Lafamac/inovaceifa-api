package com.inovaceifa.api.dto.referencia;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class ReferenciaResponseDTO {

    private Long id;
    private String descricao;
    private Boolean ativo;

    private Map<String, Object> extras;

    public ReferenciaResponseDTO extra(String key, Object value) {

        if (this.extras == null) {
            this.extras = new HashMap<>();
        }

        this.extras.put(key, value);

        return this;
    }
}