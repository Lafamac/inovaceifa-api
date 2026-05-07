package com.inovaceifa.api.service;

import com.inovaceifa.api.exception.*;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContextoFazendaService {

    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProprietarioRepository proprietarioRepository;
    private final ProprietarioFazendaAtivaRepository proprietarioFazendaAtivaRepository;
    private final FazendaRepository fazendaRepository;
    private final SafraRepository safraRepository;

    /* =========================================================
       USUÁRIO LOGADO
       ========================================================= */
    public Usuario getUsuarioLogado() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            throw new UnauthorizedException("Usuário não autenticado");
        }

        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UnauthorizedException("Usuário autenticado não encontrado"));
    }

    /* =========================================================
       FUNCIONÁRIO
       ========================================================= */
    public Optional<Funcionario> getFuncionarioLogadoOptional() {
        Usuario usuario = getUsuarioLogado();
        return funcionarioRepository.findByUsuarioId(usuario.getId());
    }

    /* =========================================================
       PROPRIETÁRIO
       ========================================================= */
    public Optional<Proprietario> getProprietarioOptional() {
        Usuario usuario = getUsuarioLogado();
        return proprietarioRepository.findByUsuario_Id(usuario.getId());
    }

    public Proprietario getProprietario() {
        return getProprietarioOptional()
                .orElseThrow(() -> new AuthException("Usuário não é proprietário"));
    }

    /* =========================================================
       DEFINIR PROPRIETÁRIO ATIVO (SUPER USUÁRIO)
       ========================================================= */
    public void definirProprietarioAtivo(Long proprietarioId) {

        Usuario usuario = getUsuarioLogado();

        if (usuario.getPerfilId() == null || usuario.getPerfilId() != 2) {
            throw new AuthException("Apenas super usuário pode definir proprietário ativo");
        }

        Proprietario proprietario = proprietarioRepository.findById(proprietarioId)
                .orElseThrow(() -> new NotFoundException("Proprietário não encontrado"));

        // Aqui não estamos persistindo contexto em tabela,
        // apenas validando que o proprietário existe.
        // O restante do fluxo depende da implementação futura
        // de contexto específico para super usuário.
    }

    /* =========================================================
       FAZENDA ATIVA (SAFE)
       ========================================================= */
    public Fazenda getFazendaAtivaOrNull() {

        Usuario usuario = getUsuarioLogado();

        if (usuario.getPerfilId() == null) {
            return null;
        }

        if (usuario.getPerfilId() == 3) {
            return funcionarioRepository.findByUsuarioId(usuario.getId())
                    .map(Funcionario::getFazenda)
                    .orElse(null);
        }

        Optional<Proprietario> proprietario =
                proprietarioRepository.findByUsuario_Id(usuario.getId());

        if (proprietario.isEmpty()) {
            return null;
        }

        return proprietarioFazendaAtivaRepository
                .findByProprietarioId(proprietario.get().getId())
                .map(ProprietarioFazendaAtiva::getFazenda)
                .orElse(null);
    }

    public Fazenda getFazendaAtiva() {

        Fazenda fazenda = getFazendaAtivaOrNull();

        if (fazenda == null) {
            throw new ContextoFazendaInexistenteException(
                    "Nenhuma fazenda ativa definida. Selecione uma fazenda."
            );
        }

        return fazenda;
    }

    /* =========================================================
       SAFRA ATIVA
       ========================================================= */
    public Safra getSafraAtivaOrNull() {

        Fazenda fazenda = getFazendaAtivaOrNull();

        if (fazenda == null) {
            return null;
        }

        return fazenda.getSafraAtiva();
    }

    public Safra getSafraAtiva() {

        Safra safra = getSafraAtivaOrNull();

        if (safra == null) {
            throw new ContextoSafraInexistenteException(
                    "Nenhuma safra ativa definida para esta fazenda."
            );
        }

        return safra;
    }

    /* =========================================================
       DEFINIR FAZENDA ATIVA
       ========================================================= */
    public void definirFazendaAtiva(Long fazendaId) {

        Proprietario proprietario = getProprietario();

        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        if (!fazenda.getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Fazenda não pertence ao proprietário");
        }

        proprietarioFazendaAtivaRepository
                .findByProprietarioId(proprietario.getId())
                .ifPresent(proprietarioFazendaAtivaRepository::delete);

        ProprietarioFazendaAtiva ativa = new ProprietarioFazendaAtiva();
        ativa.setProprietario(proprietario);
        ativa.setFazenda(fazenda);

        proprietarioFazendaAtivaRepository.save(ativa);
    }

    /* =========================================================
       DEFINIR SAFRA ATIVA
       ========================================================= */
    public void definirSafraAtiva(Long safraId) {

        Fazenda fazenda = getFazendaAtiva();

        Safra safra = safraRepository.findById(safraId)
                .orElseThrow(() -> new NotFoundException("Safra não encontrada"));

        if (!safra.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Safra não pertence à fazenda ativa");
        }

        fazenda.setSafraAtiva(safra);
        fazendaRepository.save(fazenda);
    }
}