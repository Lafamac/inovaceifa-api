package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.contapagar.*;
import com.inovaceifa.api.dto.lancamento.LancamentoCreateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.ContaPagarRepository;
import com.inovaceifa.api.repository.RefCentroCustoRepository;
import com.inovaceifa.api.repository.RefDespesaRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ContaPagarService extends BaseCrudService<ContaPagar, Long> {

    private final ContaPagarRepository contaPagarRepository;
    private final ContextoFazendaService contextoFazendaService;

    private final RefDespesaRepository refRepository;
    private final RefCentroCustoRepository centroCustoRepository;
    private final LancamentoDespesaService lancamentoService;

    @Override
    protected JpaRepository<ContaPagar, Long> getRepository() {
        return contaPagarRepository;
    }

    @Override
    protected void validarAcesso(ContaPagar conta) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!conta.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Acesso negado à conta a pagar");
        }
    }

    /* =========================================================
       🔥 NOVO MÉTODO (GET POR ID)
    ========================================================= */

    public ContaPagarResponseDTO buscar(Long id) {

        ContaPagar conta = super.buscarEntity(id);

        return toResponseDTO(conta);
    }

    /* ================= RESTO DA CLASSE SEM ALTERAÇÃO ================= */

    @Transactional
    public ContaPagar criarContaPagarProduto(
            Fazenda fazenda,
            Safra safra,
            String favorecido,
            String numeroNotaFiscal,
            BigDecimal valor,
            LocalDate dataVencimento
    ) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Valor da conta a pagar inválido");
        }

        RefDespesa ref = refRepository.findById(27L)
                .orElseThrow(() -> new AuthException("Tipo de despesa padrão não encontrado"));

        ContaPagar conta = new ContaPagar();

        conta.setFavorecido(favorecido != null ? favorecido : "Fornecedor não informado");
        conta.setFazenda(fazenda);
        conta.setSafra(safra);
        conta.setRefDespesa(ref);
        conta.setNumeroNotaFiscal(numeroNotaFiscal);
        conta.setDataVencimento(dataVencimento);
        conta.setVlrReal(valor);
        conta.setVlrJuros(BigDecimal.ZERO);
        conta.setBaixada("N");

        return super.salvarEntity(conta);
    }

    @Transactional
    public ContaPagarResponseDTO criar(ContaPagarCreateDTO dto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        RefDespesa ref = refRepository.findById(dto.getRefDespesaId())
                .orElseThrow(() -> new AuthException("Tipo de despesa não encontrado"));

        RefCentroCusto centro = centroCustoRepository.findById(dto.getCentroCustoId())
                .orElseThrow(() -> new AuthException("Centro de custo não encontrado"));

        ContaPagar conta = new ContaPagar();

        conta.setFavorecido(dto.getFavorecido());
        conta.setFazenda(fazenda);
        conta.setSafra(safra);
        conta.setRefDespesa(ref);
        conta.setCentroCusto(centro);
        conta.setNumeroNotaFiscal(dto.getNumeroNotaFiscal());
        conta.setDataVencimento(dto.getDataVencimento());
        conta.setVlrReal(dto.getVlrReal());
        conta.setVlrJuros(BigDecimal.ZERO);
        conta.setBaixada("N");

        conta = super.salvarEntity(conta);

        return toResponseDTO(conta);
    }

    public PageResponseDTO<ContaPagarResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return PageUtils.toPageResponse(
                contaPagarRepository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    public PageResponseDTO<ContaPagarResponseDTO> listarFazenda(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                contaPagarRepository.findByFazendaId(
                        fazenda.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    @Transactional
    public ContaPagarResponseDTO atualizar(Long id, ContaPagarUpdateDTO dto) {

        ContaPagar conta = super.buscarEntity(id);

        if ("S".equals(conta.getBaixada())) {
            throw new AuthException("Conta já paga não pode ser alterada");
        }

        if (dto.getFavorecido() != null)
            conta.setFavorecido(dto.getFavorecido());

        if (dto.getNumeroNotaFiscal() != null)
            conta.setNumeroNotaFiscal(dto.getNumeroNotaFiscal());

        if (dto.getDataVencimento() != null)
            conta.setDataVencimento(dto.getDataVencimento());

        if (dto.getVlrReal() != null)
            conta.setVlrReal(dto.getVlrReal());

        if (dto.getRefDespesaId() != null) {

            RefDespesa ref = refRepository.findById(dto.getRefDespesaId())
                    .orElseThrow(() -> new AuthException("Tipo de despesa não encontrado"));

            conta.setRefDespesa(ref);
        }

        if (dto.getCentroCustoId() != null) {

            RefCentroCusto centro = centroCustoRepository.findById(dto.getCentroCustoId())
                    .orElseThrow(() -> new AuthException("Centro de custo não encontrado"));

            conta.setCentroCusto(centro);
        }

        conta = super.salvarEntity(conta);

        return toResponseDTO(conta);
    }

    @Transactional
    public void excluir(Long id) {

        ContaPagar conta = super.buscarEntity(id);

        if ("S".equals(conta.getBaixada())) {
            throw new AuthException("Conta já paga não pode ser excluída");
        }

        contaPagarRepository.delete(conta);
    }

    @Transactional
    public ContaPagarResponseDTO pagar(Long id, ContaPagarPagamentoDTO dto) {

        ContaPagar conta = super.buscarEntity(id);

        if ("S".equals(conta.getBaixada())) {
            throw new AuthException("Conta já está baixada");
        }

        BigDecimal juros = dto.getVlrJuros() != null ? dto.getVlrJuros() : BigDecimal.ZERO;
        BigDecimal valorPago = conta.getVlrReal().add(juros);

        conta.setDataPagamento(dto.getDataPagamento());
        conta.setVlrJuros(juros);
        conta.setVlrPago(valorPago);
        conta.setBaixada("S");

        super.salvarEntity(conta);

        LancamentoCreateDTO lancamentoDTO = new LancamentoCreateDTO();
        lancamentoDTO.setRefDespesaId(conta.getRefDespesa().getId());
        lancamentoDTO.setCentroCustoId(
                conta.getCentroCusto() != null ? conta.getCentroCusto().getId() : null
        );
        lancamentoDTO.setValor(valorPago);
        lancamentoDTO.setData(dto.getDataPagamento());
        lancamentoDTO.setObservacao("Gerado automaticamente via Conta a Pagar");

        lancamentoService.criar(lancamentoDTO);

        return toResponseDTO(conta);
    }

    private ContaPagarResponseDTO toResponseDTO(ContaPagar conta) {

        return ContaPagarResponseDTO.builder()
                .id(conta.getId())
                .favorecido(conta.getFavorecido())
                .fazendaId(conta.getFazenda().getId())
                .safraId(conta.getSafra().getId())
                .refDespesaId(conta.getRefDespesa().getId())
                .descricaoDespesa(conta.getRefDespesa().getDescricao())
                .centroCustoId(conta.getCentroCusto() != null ? conta.getCentroCusto().getId() : null)
                .descricaoCentroCusto(conta.getCentroCusto() != null ? conta.getCentroCusto().getDescricao() : null)
                .numeroNotaFiscal(conta.getNumeroNotaFiscal())
                .dataVencimento(conta.getDataVencimento())
                .dataPagamento(conta.getDataPagamento())
                .vlrReal(conta.getVlrReal())
                .vlrJuros(conta.getVlrJuros())
                .vlrPago(conta.getVlrPago())
                .baixada(conta.getBaixada())
                .build();
    }
}