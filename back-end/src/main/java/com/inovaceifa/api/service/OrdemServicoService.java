package com.inovaceifa.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.ordemservico.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrdemServicoService extends BaseCrudService<OrdemServico, Long> {

    private final OrdemServicoRepository repository;
    private final CadastroOperacaoRepository operacaoRepository;
    private final PlanejamentoOperacaoRepository planejamentoRepository;
    private final PlanejamentoInsumoRepository planejamentoInsumoRepository;
    private final PlanejamentoMaquinaRepository planejamentoMaquinaRepository;

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final OperacaoProdutoRepository operacaoProdutoRepository;
    private final HoraMaquinaRepository horaMaquinaRepository;

    private final ProdutoRepository produtoRepository;
    private final MaquinaRepository maquinaRepository;

    private final UsuarioRepository usuarioRepository;
    private final ContextoFazendaService contexto;

    private final OrdemServicoCalculoService calculoService;
    private final OrdemServicoEstoqueService estoqueService;
    private final OrdemServicoFinanceiroService financeiroService;

    private final AuditoriaService auditoriaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected JpaRepository<OrdemServico, Long> getRepository() {
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
            throw new AuthException("Apenas super usuário pode criar ordem de serviço");
        }
    }

    @Override
    protected void validarAcesso(OrdemServico ordem) {
        Proprietario proprietario = contexto.getProprietario();

        if (!ordem.getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Acesso negado à ordem de serviço");
        }
    }

    public PageResponseDTO<OrdemServicoResponseDTO> listar(Pageable pageable) {

        Proprietario p = contexto.getProprietario();
        Fazenda f = contexto.getFazendaAtiva();
        Safra s = contexto.getSafraAtiva();

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_Id(
                        p.getId(), f.getId(), s.getId(), pageable
                ),
                this::toResponse
        );
    }

    public OrdemServicoResponseDTO buscarPorId(Long id) {
        return toResponse(super.buscarEntity(id));
    }

    public OrdemServicoResponseDTO criar(OrdemServicoCreateDTO dto) {

        validarSuperUsuario();

        CadastroOperacao operacao = operacaoRepository.findById(dto.getOperacaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        OrdemServico ordem = new OrdemServico();

        ordem.setOperacao(operacao);
        ordem.setDataInicio(dto.getDataInicio());
        ordem.setDataFim(dto.getDataFim());
        ordem.setStatus(dto.getStatus());
        ordem.setObservacao(dto.getObservacao());
        ordem.setCustoTotal(dto.getCustoTotal());

        ordem.setProprietario(contexto.getProprietario());
        ordem.setFazenda(contexto.getFazendaAtiva());
        ordem.setSafra(contexto.getSafraAtiva());

        ordem = super.salvarEntity(ordem);

        ordem.setNrOs(String.format("%06d", ordem.getId()));
        ordem = super.salvarEntity(ordem);

        Usuario usuario = getUsuarioLogado();
        auditoriaService.registrar(
                ordem.getId(),
                usuario.getId(),
                "CREATE",
                null,
                toJson(ordem)
        );

        return toResponse(ordem);
    }

    public OrdemServicoResponseDTO atualizar(Long id, OrdemServicoUpdateDTO dto) {

        OrdemServico ordem = super.buscarEntity(id);

        String antes = toJson(ordem);

        if (dto.getVersion() == null || !dto.getVersion().equals(ordem.getVersion())) {
            throw new AuthException("Registro alterado por outro usuário");
        }

        if (dto.getPlanejamentoOperacaoId() != null) {
            PlanejamentoOperacao planejamento = planejamentoRepository.findById(dto.getPlanejamentoOperacaoId())
                    .orElseThrow(() -> new AuthException("Planejamento não encontrado"));

            ordem.setPlanejamentoOperacao(planejamento);
        }

        ordem.setDataInicio(dto.getDataInicio());
        ordem.setDataFim(dto.getDataFim());
        ordem.setStatus(dto.getStatus());
        ordem.setObservacao(dto.getObservacao());
        ordem.setCustoTotal(dto.getCustoTotal());

        ordem = super.salvarEntity(ordem);

        String depois = toJson(ordem);
        Usuario usuario = getUsuarioLogado();

        auditoriaService.registrar(
                ordem.getId(),
                usuario.getId(),
                "UPDATE",
                antes,
                depois
        );

        return toResponse(ordem);
    }

    public void excluir(Long id) {

        OrdemServico ordem = super.buscarEntity(id);

        String antes = toJson(ordem);
        Usuario usuario = getUsuarioLogado();

        repository.delete(ordem);

        auditoriaService.registrar(
                ordem.getId(),
                usuario.getId(),
                "DELETE",
                antes,
                null
        );
    }

    /* =========================================================
       🔥 GERAR OS COM VALIDAÇÃO DE DUPLICIDADE
       ========================================================= */

    @Transactional
    public OrdemServicoResponseDTO gerarDePlanejamentos(List<Long> planejamentoIds) {

        validarSuperUsuario();

        if (planejamentoIds == null || planejamentoIds.isEmpty()) {
            throw new AuthException("Nenhum planejamento informado");
        }

        List<PlanejamentoOperacao> planejamentos = planejamentoRepository.findAllById(planejamentoIds);

        if (planejamentos.isEmpty()) {
            throw new AuthException("Planejamentos não encontrados");
        }

        for (PlanejamentoOperacao p : planejamentos) {

            boolean existe = operacaoTalhaoRepository
                    .existsBySafraTalhao_IdAndOrdemServico_StatusNot(
                            p.getSafraTalhao().getId(),
                            "FINALIZADA"
                    );

            if (existe) {
                throw new AuthException(
                        "Já existe uma OS ativa para o talhão: " + p.getSafraTalhao().getId()
                );
            }
        }

        Fazenda fazenda = contexto.getFazendaAtiva();

        OrdemServico os = new OrdemServico();

        os.setProprietario(contexto.getProprietario());
        os.setFazenda(fazenda);
        os.setSafra(contexto.getSafraAtiva());
        os.setOperacao(planejamentos.get(0).getOperacao());
        os.setStatus("ABERTA");
        os.setObservacao("Gerada via planejamento");

        os = repository.save(os);
        os.setNrOs(String.format("%06d", os.getId()));
        os = repository.save(os);

        Map<Long, OperacaoTalhao> mapaTalhoes = new HashMap<>();

        for (PlanejamentoOperacao p : planejamentos) {

            OperacaoTalhao t = new OperacaoTalhao();
            t.setOrdemServico(os);
            t.setSafraTalhao(p.getSafraTalhao());
            t.setAreaTrabalhada(p.getAreaPlanejada());
            t.setDataExecucao(p.getDataPrevista());
            t.setProprietario(os.getProprietario());
            t.setFazenda(os.getFazenda());
            t.setSafra(os.getSafra());

            t = operacaoTalhaoRepository.save(t);
            mapaTalhoes.put(p.getId(), t);
        }

        BigDecimal custoTotal = BigDecimal.ZERO;

        for (PlanejamentoOperacao p : planejamentos) {

            OperacaoTalhao talhao = mapaTalhoes.get(p.getId());

            List<PlanejamentoInsumo> lista =
                    planejamentoInsumoRepository.findByPlanejamentoOperacaoIdAndAtivoTrue(p.getId());

            for (PlanejamentoInsumo i : lista) {

                OperacaoProduto op = new OperacaoProduto();

                op.setProprietario(os.getProprietario());
                op.setFazenda(os.getFazenda());
                op.setSafra(os.getSafra());
                op.setOperacaoTalhao(talhao);
                op.setProduto(i.getProduto());
                op.setQuantidade(i.getQuantidadeTotal());
                op.setVlrUnitario(i.getValorUnitarioPrevisto());
                op.setVlrTotal(i.getValorTotalPrevisto());

                operacaoProdutoRepository.save(op);

                if (i.getValorTotalPrevisto() != null) {
                    custoTotal = custoTotal.add(i.getValorTotalPrevisto());
                }
            }
        }

        for (PlanejamentoOperacao p : planejamentos) {

            List<PlanejamentoMaquina> maquinas =
                    planejamentoMaquinaRepository.findByPlanejamentoOperacaoIdAndAtivoTrue(p.getId());

            for (PlanejamentoMaquina m : maquinas) {

                HoraMaquina hm = new HoraMaquina();

                hm.setMaquina(m.getMaquina());
                hm.setHorimetroInicial(BigDecimal.ZERO);
                hm.setHorimetroFinal(m.getHorasPrevistas());
                hm.setHorasTrabalhadas(m.getHorasPrevistas());

                horaMaquinaRepository.save(hm);

                if (m.getCustoTotal() != null) {
                    custoTotal = custoTotal.add(m.getCustoTotal());
                }
            }
        }

        os.setCustoTotal(custoTotal);
        repository.save(os);

        for (PlanejamentoOperacao p : planejamentos) {
            p.setStatus("EXECUTADO");
            planejamentoRepository.save(p);
        }

        return toResponse(os);
    }

    @Transactional
    public void finalizarOrdemServico(Long ordemId) {

        OrdemServico ordem = super.buscarEntity(ordemId);

        if ("FINALIZADA".equalsIgnoreCase(ordem.getStatus())) {
            throw new AuthException("Já finalizada");
        }

        BigDecimal precoDiesel = BigDecimal.valueOf(6.50);

        BigDecimal custoTotal =
                calculoService.calcularCustoTotal(ordemId, precoDiesel);

        estoqueService.gerarConsumoDiesel(ordem, precoDiesel);
        financeiroService.gerarLancamentoDespesa(ordem, custoTotal);

        ordem.setStatus("FINALIZADA");
        ordem.setDataFim(LocalDate.now());
        ordem.setCustoTotal(custoTotal);

        super.salvarEntity(ordem);
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }

    /* =========================================================
   ADICIONAR MÁQUINAS NA OS
   ========================================================= */

    public void adicionarMaquinas(Long ordemId, OrdemServicoMaquinasDTO dto) {

        OrdemServico ordem = super.buscarEntity(ordemId);

        validarAcesso(ordem);

        if (dto.getMaquinas() == null || dto.getMaquinas().isEmpty()) {
            throw new AuthException("Nenhuma máquina informada");
        }

        for (OrdemServicoMaquinasDTO.MaquinaItem item : dto.getMaquinas()) {

            Maquina maquina = maquinaRepository.findById(item.getMaquinaId())
                    .orElseThrow(() -> new AuthException("Máquina não encontrada"));

            HoraMaquina hm = new HoraMaquina();

            hm.setMaquina(maquina);
            hm.setHorimetroInicial(item.getHorimetroInicial());
            hm.setHorimetroFinal(item.getHorimetroFinal());

            if (item.getHorimetroInicial() != null && item.getHorimetroFinal() != null) {
                hm.setHorasTrabalhadas(
                        item.getHorimetroFinal().subtract(item.getHorimetroInicial())
                );
            }

            horaMaquinaRepository.save(hm);
        }
    }

/* =========================================================
   ADICIONAR FUNCIONÁRIOS NA OS
   ========================================================= */

    public void adicionarFuncionarios(Long ordemId, OrdemServicoFuncionariosDTO dto) {

        OrdemServico ordem = super.buscarEntity(ordemId);

        validarAcesso(ordem);

        if (dto.getFuncionarios() == null || dto.getFuncionarios().isEmpty()) {
            throw new AuthException("Nenhum funcionário informado");
        }

        for (OrdemServicoFuncionariosDTO.FuncionarioItem item : dto.getFuncionarios()) {

            if (!usuarioRepository.existsById(item.getFuncionarioId())) {
                throw new AuthException("Funcionário não encontrado");
            }
        }
    }

    private OrdemServicoResponseDTO toResponse(OrdemServico o) {

        // 🔥 NOVO: buscar talhões da OS
        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(o.getId());

        return OrdemServicoResponseDTO.builder()
                .id(o.getId())
                .nrOs(o.getNrOs())
                .version(o.getVersion())
                .operacaoId(o.getOperacao().getId())
                .operacaoNome(o.getOperacao().getOperacao())
                .planejamentoOperacaoId(
                        o.getPlanejamentoOperacao() != null ? o.getPlanejamentoOperacao().getId() : null
                )
                .dataInicio(o.getDataInicio())
                .dataFim(o.getDataFim())
                .status(o.getStatus())
                .observacao(o.getObservacao())
                .custoTotal(o.getCustoTotal())
                .proprietarioId(o.getProprietario().getId())
                .fazendaId(o.getFazenda().getId())
                .safraId(o.getSafra().getId())

                // 🔥 NOVO: adicionar talhões no response
                .talhoes(
                        talhoes.stream()
                                .map(t -> new OrdemServicoResponseDTO.TalhaoDTO(
                                        t.getSafraTalhao().getId(),
                                        t.getSafraTalhao().getTalhao().getNome()
                                ))
                                .toList()
                )

                .build();
    }
}