package com.inovaceifa.api.service;

import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.RefParametro;
import com.inovaceifa.api.repository.RefParametroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ParametroService {

    private final RefParametroRepository repository;

    public BigDecimal getPercentualEncargos() {

        BigDecimal percentual = repository
                .findByChaveAndAtivoTrue("PERCENTUAL_ENCARGOS")
                .map(RefParametro::getValor)
                .orElseThrow(() -> new AuthException(
                        "Parâmetro PERCENTUAL_ENCARGOS não configurado"
                ));

        /* 🔥 NORMALIZAÇÃO (50 → 0.5) */

        if (percentual.compareTo(BigDecimal.ONE) > 0) {
            percentual = percentual.divide(BigDecimal.valueOf(100));
        }

        return percentual;
    }
}