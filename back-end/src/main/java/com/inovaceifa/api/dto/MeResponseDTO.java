package com.inovaceifa.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MeResponseDTO {

    /* =========================
       DADOS DO USUÁRIO
       ========================= */
    private Long id;
    private String nome;
    private String email;
    private Long perfilId;

    /* =========================
       NOVO - TIPO DE USUÁRIO
       ========================= */
    private String tipo; // PROPRIETARIO | FUNCIONARIO

    /* =========================
       CONTEXTO DE FAZENDA
       ========================= */
    private FazendaDTO fazendaAtiva;
    private List<FazendaDTO> fazendas;

    /* =========================
       DTO INTERNO - FAZENDA
       ========================= */
    @Data
    @Builder
    public static class FazendaDTO {
        private Long id;
        private String nome;
    }
}
