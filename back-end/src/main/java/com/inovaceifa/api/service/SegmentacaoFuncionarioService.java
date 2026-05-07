package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.segmentacao.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SegmentacaoFuncionarioService {

    private final SegmentacaoFuncionarioRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final CadastroOperacaoRepository operacaoRepository;
    private final FolhaPagamentoRepository folhaRepository;
    private final ContextoFazendaService contexto;

    /* =========================================================
       CRIAR (COM VALIDAÇÃO 100%)
       ========================================================= */

    public SegmentacaoFuncionarioResponseDTO criar(SegmentacaoFuncionarioCreateDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new AuthException("Funcionário não encontrado"));

        CadastroOperacao operacao = operacaoRepository.findById(dto.getOperacaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        List<SegmentacaoFuncionario> existentes =
                repository.findByFuncionario_IdAndSafra_IdAndAtivoTrue(
                        funcionario.getId(),
                        contexto.getSafraAtiva().getId()
                );

        BigDecimal total = dto.getPercentual();

        for (SegmentacaoFuncionario s : existentes) {
            total = total.add(s.getPercentual());
        }

        if (total.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new AuthException("Percentual excede 100%");
        }

        SegmentacaoFuncionario s = new SegmentacaoFuncionario();

        s.setFuncionario(funcionario);
        s.setOperacao(operacao);
        s.setPercentual(dto.getPercentual());

        s.setProprietario(contexto.getProprietario());
        s.setFazenda(contexto.getFazendaAtiva());
        s.setSafra(contexto.getSafraAtiva());

        s.setAtivo(true);

        return toDTO(repository.save(s));
    }

    /* =========================================================
       VALIDAR 100% (OPCIONAL PARA FECHAMENTO)
       ========================================================= */

    public void validarFechamento(Long funcionarioId) {

        List<SegmentacaoFuncionario> lista =
                repository.findByFuncionario_IdAndSafra_IdAndAtivoTrue(
                        funcionarioId,
                        contexto.getSafraAtiva().getId()
                );

        BigDecimal total = BigDecimal.ZERO;

        for (SegmentacaoFuncionario s : lista) {
            total = total.add(s.getPercentual());
        }

        if (total.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new AuthException("Segmentação deve totalizar 100%");
        }
    }

    /* =========================================================
       🔥 CÁLCULO DISTRIBUÍDO (CORE)
       ========================================================= */

    public Map<Long, BigDecimal> calcularCustoPorOperacao() {

        Safra safra = contexto.getSafraAtiva();

        List<FolhaPagamento> folhas =
                folhaRepository.findBySafra_Id(safra.getId());

        Map<Long, BigDecimal> custoPorOperacao = new HashMap<>();

        for (FolhaPagamento folha : folhas) {

            BigDecimal salarioTotal = folha.getSalarioTotal(); // 🔥 já com encargos

            List<SegmentacaoFuncionario> segmentacoes =
                    repository.findByFuncionario_IdAndSafra_IdAndAtivoTrue(
                            folha.getFuncionario().getId(),
                            safra.getId()
                    );

            for (SegmentacaoFuncionario s : segmentacoes) {

                BigDecimal percentual = s.getPercentual()
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

                BigDecimal custo = salarioTotal.multiply(percentual);

                Long operacaoId = s.getOperacao().getId();

                custoPorOperacao.merge(
                        operacaoId,
                        custo,
                        BigDecimal::add
                );
            }
        }

        return custoPorOperacao;
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public List<SegmentacaoFuncionarioResponseDTO> listar(Long funcionarioId) {

        return repository.findByFuncionario_IdAndSafra_IdAndAtivoTrue(
                        funcionarioId,
                        contexto.getSafraAtiva().getId()
                )
                .stream()
                .map(this::toDTO)
                .toList();
    }

    /* =========================================================
       DTO
       ========================================================= */

    private SegmentacaoFuncionarioResponseDTO toDTO(SegmentacaoFuncionario s) {

        return SegmentacaoFuncionarioResponseDTO.builder()
                .id(s.getId())
                .funcionarioId(s.getFuncionario().getId())
                .funcionarioNome(s.getFuncionario().getNome())
                .operacaoId(s.getOperacao().getId())
                .operacaoNome(s.getOperacao().getOperacao())
                .percentual(s.getPercentual())
                .ativo(s.getAtivo())
                .build();
    }
}