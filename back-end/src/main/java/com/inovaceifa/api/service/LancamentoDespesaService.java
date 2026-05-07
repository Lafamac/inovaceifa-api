package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.financeiro.*;
import com.inovaceifa.api.dto.lancamento.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.LancamentoDespesaRepository;
import com.inovaceifa.api.repository.RefCentroCustoRepository;
import com.inovaceifa.api.repository.RefDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LancamentoDespesaService extends BaseCrudService<LancamentoDespesa, Long> {

    private final LancamentoDespesaRepository repository;
    private final RefDespesaRepository refRepository;
    private final RefCentroCustoRepository centroCustoRepository;
    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<LancamentoDespesa, Long> getRepository() {
        return repository;
    }

    /* =========================================================
       VALIDAR ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(LancamentoDespesa l) {

        Safra safra = contexto.getSafraAtiva();

        if (!l.getSafra().getId().equals(safra.getId())) {
            throw new AuthException("Lançamento não pertence à safra ativa");
        }
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public LancamentoResponseDTO criar(LancamentoCreateDTO dto) {

        Proprietario proprietario = contexto.getProprietario();
        Fazenda fazenda = contexto.getFazendaAtiva();
        Safra safra = contexto.getSafraAtiva();

        RefDespesa ref = refRepository.findById(dto.getRefDespesaId())
                .orElseThrow(() -> new AuthException("Tipo de despesa não encontrado"));

        RefCentroCusto centro = centroCustoRepository.findById(dto.getCentroCustoId())
                .orElseThrow(() -> new AuthException("Centro de custo não encontrado"));

        LancamentoDespesa l = new LancamentoDespesa();

        l.setProprietario(proprietario);
        l.setFazenda(fazenda);
        l.setSafra(safra);
        l.setRefDespesa(ref);
        l.setCentroCusto(centro);
        l.setValor(dto.getValor());
        l.setData(dto.getData());
        l.setOrigem("MANUAL");
        l.setObservacao(dto.getObservacao());
        l.setCriadoEm(LocalDateTime.now());
        l.setStatusPagamento("PENDENTE");
        l.setAtivo(true);

        l = super.salvarEntity(l);

        return toResponse(l);
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public List<LancamentoResponseDTO> listar(
            String status,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        Safra safra = contexto.getSafraAtiva();

        return repository.filtrar(
                safra.getId(),
                status,
                dataInicio,
                dataFim
        ).stream().map(this::toResponse).toList();
    }

    /* =========================================================
       MARCAR COMO PAGO
       ========================================================= */

    public void marcarComoPago(Long id) {

        LancamentoDespesa l = super.buscarEntity(id);

        l.setStatusPagamento("PAGO");

        super.salvarEntity(l);
    }

    /* =========================================================
       CANCELAR
       ========================================================= */

    public void cancelar(Long id) {

        LancamentoDespesa l = super.buscarEntity(id);

        l.setAtivo(false);

        super.salvarEntity(l);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    public void reativar(Long id) {

        LancamentoDespesa l = repository.findById(id)
                .orElseThrow(() -> new AuthException("Lançamento não encontrado"));

        l.setAtivo(true);

        super.salvarEntity(l);
    }

    /* =========================================================
       RESUMO
       ========================================================= */

    public FinanceiroResumoDTO gerarResumoPorPeriodo(
            LocalDate dataInicio,
            LocalDate dataFim
    ) {

        Safra safra = contexto.getSafraAtiva();

        BigDecimal total = repository.somarTotalDespesasPeriodo(
                safra.getId(), dataInicio, dataFim);

        BigDecimal totalPago = repository.somarTotalPagoPeriodo(
                safra.getId(), dataInicio, dataFim);

        Long quantidade = repository.contarLancamentosPeriodo(
                safra.getId(), dataInicio, dataFim);

        BigDecimal totalPendente = total.subtract(totalPago);

        return FinanceiroResumoDTO.builder()
                .totalDespesas(total)
                .totalPago(totalPago)
                .totalPendente(totalPendente)
                .quantidadeLancamentos(quantidade)
                .build();
    }

    /* =========================================================
       DASHBOARD FINANCEIRO
       ========================================================= */

    public DashboardFinanceiroDTO gerarDashboard() {

        Safra safra = contexto.getSafraAtiva();

        BigDecimal totalGeral = repository.somarTotalDespesas(safra.getId());

        List<DashboardCategoriaDTO> categorias =
                repository.agruparPorCategoria(safra.getId())
                        .stream()
                        .map(obj -> {

                            Long refId = (Long) obj[0];
                            String descricao = (String) obj[1];
                            BigDecimal total = (BigDecimal) obj[2];

                            BigDecimal percentual = BigDecimal.ZERO;

                            if (totalGeral.compareTo(BigDecimal.ZERO) > 0) {
                                percentual = total
                                        .divide(totalGeral, 4, RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100));
                            }

                            return DashboardCategoriaDTO.builder()
                                    .refDespesaId(refId)
                                    .descricao(descricao)
                                    .total(total)
                                    .percentual(percentual)
                                    .build();
                        })
                        .toList();

        return DashboardFinanceiroDTO.builder()
                .totalGeral(totalGeral)
                .categorias(categorias)
                .build();
    }

    /* =========================================================
       DASHBOARD MENSAL
       ========================================================= */

    public List<DashboardMensalDTO> gerarDashboardMensal() {

        Safra safra = contexto.getSafraAtiva();

        return repository.agruparPorMes(safra.getId())
                .stream()
                .map(obj -> DashboardMensalDTO.builder()
                        .ano(((Number) obj[0]).longValue())
                        .mes(((Number) obj[1]).longValue())
                        .total((BigDecimal) obj[2])
                        .build())
                .toList();
    }

    /* =========================================================
       CUSTO POR HECTARE
       ========================================================= */

    public BigDecimal calcularCustoPorHectare() {

        Safra safra = contexto.getSafraAtiva();

        BigDecimal total = repository.somarTotalDespesas(safra.getId());
        BigDecimal area = safra.getAreaPlantada();

        if (area == null || area.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return total.divide(area, 4, RoundingMode.HALF_UP);
    }

    /* =========================================================
       ORÇADO VS REALIZADO
       ========================================================= */

    public OrcadoVsRealizadoDTO gerarOrcadoVsRealizado() {

        Safra safra = contexto.getSafraAtiva();

        BigDecimal realizado = repository.somarRealizado(safra.getId());
        BigDecimal orcado = safra.getOrcamentoPrevisto();

        if (orcado == null) {
            orcado = BigDecimal.ZERO;
        }

        BigDecimal diferenca = realizado.subtract(orcado);

        return OrcadoVsRealizadoDTO.builder()
                .orcado(orcado)
                .realizado(realizado)
                .diferenca(diferenca)
                .build();
    }

    /* =========================================================
       PROJEÇÃO SAFRA
       ========================================================= */

    public ProjecaoSafraDTO gerarProjecaoSafra() {

        Safra safra = contexto.getSafraAtiva();

        BigDecimal realizado = repository.somarRealizado(safra.getId());

        long mesesPassados = java.time.Period
                .between(safra.getDataInicial(), LocalDate.now())
                .toTotalMonths();

        mesesPassados = Math.max(1, mesesPassados);

        BigDecimal mediaMensal = realizado.divide(
                BigDecimal.valueOf(mesesPassados),
                4,
                RoundingMode.HALF_UP
        );

        long mesesTotais = java.time.Period
                .between(safra.getDataInicial(), safra.getDataFinal())
                .toTotalMonths();

        mesesTotais = Math.max(1, mesesTotais);

        BigDecimal projecaoFinal = mediaMensal.multiply(
                BigDecimal.valueOf(mesesTotais)
        );

        return ProjecaoSafraDTO.builder()
                .realizadoAteAgora(realizado)
                .mediaMensal(mediaMensal)
                .projecaoFinal(projecaoFinal)
                .build();
    }

    /* =========================================================
       DTO
       ========================================================= */

    private LancamentoResponseDTO toResponse(LancamentoDespesa l) {

        return LancamentoResponseDTO.builder()
                .id(l.getId())
                .refDespesaId(l.getRefDespesa().getId())
                .descricaoDespesa(l.getRefDespesa().getDescricao())
                .centroCustoId(l.getCentroCusto() != null ? l.getCentroCusto().getId() : null)
                .descricaoCentroCusto(l.getCentroCusto() != null ? l.getCentroCusto().getDescricao() : null)
                .valor(l.getValor())
                .data(l.getData())
                .origem(l.getOrigem())
                .statusPagamento(l.getStatusPagamento())
                .build();
    }
}