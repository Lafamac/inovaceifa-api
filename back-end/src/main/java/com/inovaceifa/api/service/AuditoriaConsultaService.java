package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.auditoria.AuditoriaOrdemServicoResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.AuditoriaOrdemServicoRepository;
import com.inovaceifa.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditoriaConsultaService {

    private final AuditoriaOrdemServicoRepository repository;
    private final UsuarioRepository usuarioRepository;

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

    /* ========================= CONSULTA ========================= */

    public List<AuditoriaOrdemServicoResponseDTO> listarPorOrdem(Long ordemId) {

        validarSuperUsuario(); // 🔒

        return repository.findByOrdemServicoIdOrderByDataEventoDesc(ordemId)
                .stream()
                .map(a -> AuditoriaOrdemServicoResponseDTO.builder()
                        .id(a.getId())
                        .ordemServicoId(a.getOrdemServicoId())
                        .usuarioId(a.getUsuarioId())
                        .acao(a.getAcao())
                        .dadosAntes(a.getDadosAntes())
                        .dadosDepois(a.getDadosDepois())
                        .dataEvento(a.getDataEvento())
                        .build()
                )
                .toList();
    }
}