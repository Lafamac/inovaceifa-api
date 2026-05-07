package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.planejamento.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanejamentoOperacaoService extends BaseCrudService<PlanejamentoOperacao, Long> {

    private final PlanejamentoOperacaoRepository repository;
    private final CadastroOperacaoRepository operacaoRepository;
    private final SafraTalhaoRepository safraTalhaoRepository;
    private final PlanejamentoInsumoRepository planejamentoInsumoRepository;
    private final PlanejamentoMaquinaRepository planejamentoMaquinaRepository;
    private final PlanejamentoFuncionarioRepository planejamentoFuncionarioRepository;
    private final ContextoFazendaService contexto;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected JpaRepository<PlanejamentoOperacao, Long> getRepository() {
        return repository;
    }

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
            throw new AuthException("Apenas super usuário pode realizar esta operação");
        }
    }

    @Override
    protected void validarAcesso(PlanejamentoOperacao p) {
        Fazenda fazenda = contexto.getFazendaAtiva();
        if (!p.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Planejamento não pertence à fazenda ativa");
        }
    }

    public PageResponseDTO<PlanejamentoOperacaoResponseDTO> listar(Pageable pageable) {

        Proprietario p = contexto.getProprietario();
        Fazenda f = contexto.getFazendaAtiva();
        Safra s = contexto.getSafraAtiva();

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_Id(
                        p.getId(), f.getId(), s.getId(), pageable
                ),
                this::toResponseDTO
        );
    }

    public PlanejamentoOperacaoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(super.buscarEntity(id));
    }

    @Transactional
    public PlanejamentoOperacaoResponseDTO criar(PlanejamentoOperacaoCreateDTO dto) {

        validarSuperUsuario();

        if (dto.getAreaPlanejada() == null ||
                dto.getAreaPlanejada().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Área planejada inválida");
        }

        if (dto.getSafraTalhaoId() == null || dto.getSafraTalhaoId() <= 0) {
            throw new AuthException("Safra Talhão inválido");
        }

        Optional<PlanejamentoOperacao> existente =
                repository.findBySafraTalhaoId(dto.getSafraTalhaoId());

        if (existente.isPresent()) {
            throw new AuthException("Já existe planejamento para este talhão nesta safra");
        }

        CadastroOperacao operacao = operacaoRepository.findById(dto.getOperacaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        SafraTalhao safraTalhao = safraTalhaoRepository.findById(dto.getSafraTalhaoId())
                .orElseThrow(() -> new AuthException("Safra talhão não encontrada"));

        PlanejamentoOperacao p = new PlanejamentoOperacao();

        p.setOperacao(operacao);
        p.setSafraTalhao(safraTalhao);
        p.setDataPrevista(dto.getDataPrevista());
        p.setAreaPlanejada(dto.getAreaPlanejada());

        p.setProprietario(contexto.getProprietario());
        p.setFazenda(contexto.getFazendaAtiva());
        p.setSafra(contexto.getSafraAtiva());

        BigDecimal area = dto.getAreaPlanejada();

        BigDecimal velocidade = operacao.getVelocidadeOp();
        BigDecimal eficiencia = operacao.getEficienciaCampo();
        BigDecimal diesel = operacao.getGastoDiesel();

        if (velocidade != null && eficiencia != null &&
                velocidade.compareTo(BigDecimal.ZERO) > 0 &&
                eficiencia.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal horas = area.divide(
                    velocidade.multiply(eficiencia),
                    2,
                    RoundingMode.HALF_UP
            );

            p.setHorasPrevistas(horas);
            p.setVelocidade(velocidade);
            p.setEficiencia(eficiencia);
        }

        if (diesel != null) {
            p.setDieselPrevisto(diesel.multiply(area));
        }

        p.setStatus("PLANEJADO");
        p.setAtivo(true);

        try {
            return toResponseDTO(super.salvarEntity(p));
        } catch (DataIntegrityViolationException e) {
            throw new AuthException("Já existe planejamento para este talhão nesta safra");
        }
    }

    public PlanejamentoOperacaoResponseDTO buscarPorSafraTalhao(Long safraTalhaoId) {

        PlanejamentoOperacao planejamento = repository
                .findBySafraTalhaoId(safraTalhaoId)
                .orElseThrow(() -> new AuthException("Planejamento não encontrado para este talhão"));

        validarAcesso(planejamento);

        return toResponseDTO(planejamento);
    }

    public void excluir(Long id) {
        validarSuperUsuario();

        PlanejamentoOperacao p = super.buscarEntity(id);
        p.setAtivo(false);

        super.salvarEntity(p);
    }

    private PlanejamentoOperacaoResponseDTO toResponseDTO(PlanejamentoOperacao p) {

        return PlanejamentoOperacaoResponseDTO.builder()
                .id(p.getId())
                .operacaoId(p.getOperacao().getId())
                .safraTalhaoId(p.getSafraTalhao().getId())
                .dataPrevista(p.getDataPrevista())
                .areaPlanejada(p.getAreaPlanejada())
                .horasPrevistas(p.getHorasPrevistas())
                .dieselPrevisto(p.getDieselPrevisto())
                .status(p.getStatus())
                .build();
    }
}