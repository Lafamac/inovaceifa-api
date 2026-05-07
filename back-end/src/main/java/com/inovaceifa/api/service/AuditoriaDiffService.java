package com.inovaceifa.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inovaceifa.api.dto.auditoria.AuditoriaDiffDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.AuditoriaOrdemServico;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.AuditoriaOrdemServicoRepository;
import com.inovaceifa.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuditoriaDiffService {

    private final AuditoriaOrdemServicoRepository repository;
    private final UsuarioRepository usuarioRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /* ========================= SEGURANÇA ========================= */

    private Usuario getUsuarioLogado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthException("Usuário não autenticado");
        }

        String email = (String) authentication.getPrincipal();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));
    }

    private void validarSuperUsuario() {
        Usuario usuario = getUsuarioLogado();

        if (usuario.getPerfilId() == null || usuario.getPerfilId() != 2) {
            throw new AuthException("Acesso restrito ao super usuário");
        }
    }

    /* ========================= DIFF ========================= */

    public List<AuditoriaDiffDTO> listarDiffPorOrdem(Long ordemId) {

        validarSuperUsuario(); // 🔒

        List<AuditoriaOrdemServico> logs =
                repository.findByOrdemServicoIdOrderByDataEventoDesc(ordemId);

        List<AuditoriaDiffDTO> diffs = new ArrayList<>();

        for (AuditoriaOrdemServico log : logs) {

            Map<String, Object> antes = toMap(log.getDadosAntes());
            Map<String, Object> depois = toMap(log.getDadosDepois());

            Usuario usuario = usuarioRepository.findById(log.getUsuarioId())
                    .orElse(null);

            String nome = usuario != null ? usuario.getNome() : "Desconhecido";

            Set<String> campos = new HashSet<>();
            if (antes != null) campos.addAll(antes.keySet());
            if (depois != null) campos.addAll(depois.keySet());

            for (String campo : campos) {

                Object vAntes = antes != null ? antes.get(campo) : null;
                Object vDepois = depois != null ? depois.get(campo) : null;

                if (!Objects.equals(vAntes, vDepois)) {

                    diffs.add(
                            AuditoriaDiffDTO.builder()
                                    .campo(campo)
                                    .antes(vAntes != null ? vAntes.toString() : null)
                                    .depois(vDepois != null ? vDepois.toString() : null)
                                    .usuarioId(log.getUsuarioId())
                                    .usuarioNome(nome)
                                    .dataEvento(log.getDataEvento())
                                    .build()
                    );
                }
            }
        }

        return diffs;
    }

    private Map<String, Object> toMap(String json) {
        try {
            if (json == null) return null;

            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return null;
        }
    }
}