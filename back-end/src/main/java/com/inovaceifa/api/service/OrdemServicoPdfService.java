package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.relatorio.OrdemServicoCombustivelDTO;
import com.inovaceifa.api.dto.relatorio.OrdemServicoMaquinaDTO;
import com.inovaceifa.api.dto.relatorio.OrdemServicoProdutoDTO;
import com.inovaceifa.api.model.OperacaoCombustivel;
import com.inovaceifa.api.model.OperacaoProduto;
import com.inovaceifa.api.model.OperacaoTalhao;
import com.inovaceifa.api.model.HoraMaquina;
import com.inovaceifa.api.repository.OperacaoCombustivelRepository;
import com.inovaceifa.api.repository.OperacaoProdutoRepository;
import com.inovaceifa.api.repository.OperacaoTalhaoRepository;
import com.inovaceifa.api.repository.HoraMaquinaRepository;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdemServicoPdfService {

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final OperacaoProdutoRepository operacaoProdutoRepository;
    private final OperacaoCombustivelRepository operacaoCombustivelRepository;
    private final HoraMaquinaRepository horaMaquinaRepository;
    private final TemplateEngine templateEngine;

    public byte[] gerarPdf(Long id) {

        /* ================= OPERAÇÃO ================= */

        OperacaoTalhao op = operacaoTalhaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operação não encontrada"));

        /* ================= PRODUTOS ================= */

        List<OrdemServicoProdutoDTO> produtos =
                operacaoProdutoRepository.findByOperacaoTalhao_Id(id)
                        .stream()
                        .map(p -> OrdemServicoProdutoDTO.builder()
                                .nome(p.getProduto().getNome())
                                .quantidade(p.getQuantidade())
                                .valorUnitario(p.getVlrUnitario())
                                .valorTotal(p.getVlrTotal())
                                .build())
                        .collect(Collectors.toList());

        BigDecimal totalProdutos = produtos.stream()
                .map(p -> p.getValorTotal() != null ? p.getValorTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* ================= COMBUSTÍVEL ================= */

        List<OrdemServicoCombustivelDTO> combustiveis =
                operacaoCombustivelRepository.findByOperacaoTalhao_Id(id)
                        .stream()
                        .map(c -> {
                            BigDecimal total = BigDecimal.ZERO;

                            if (c.getLitros() != null && c.getValorUnitario() != null) {
                                total = c.getLitros().multiply(c.getValorUnitario());
                            }

                            return OrdemServicoCombustivelDTO.builder()
                                    .maquina(c.getMaquina().getNome())
                                    .litros(c.getLitros())
                                    .valorUnitario(c.getValorUnitario())
                                    .valorTotal(total)
                                    .build();
                        })
                        .collect(Collectors.toList());

        BigDecimal totalCombustivel = combustiveis.stream()
                .map(c -> c.getValorTotal() != null ? c.getValorTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* ================= MÁQUINAS ================= */

        List<HoraMaquina> horasMaquinas;

        if (op.getOrdemServico() != null) {
            horasMaquinas = horaMaquinaRepository
                    .findByOperacaoTalhao_OrdemServico_Id(op.getOrdemServico().getId());
        } else {
            horasMaquinas = List.of();
        }

        List<OrdemServicoMaquinaDTO> maquinas =
                horasMaquinas.stream()
                        .map(h -> {
                            BigDecimal total = BigDecimal.ZERO;

                            if (h.getHorasTrabalhadas() != null && h.getCustoHora() != null) {
                                total = h.getHorasTrabalhadas().multiply(h.getCustoHora());
                            }

                            return OrdemServicoMaquinaDTO.builder()
                                    .maquina(h.getMaquina().getNome())
                                    .operador(h.getFuncionario() != null ? h.getFuncionario().getNome() : "")
                                    .horas(h.getHorasTrabalhadas())
                                    .custoHora(h.getCustoHora())
                                    .custoTotal(total)
                                    .build();
                        })
                        .collect(Collectors.toList());

        BigDecimal totalMaquinas = maquinas.stream()
                .map(m -> m.getCustoTotal() != null ? m.getCustoTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        /* ================= CONTEXTO ================= */

        Context context = new Context();

        context.setVariable("fazenda", op.getFazenda().getNome());
        context.setVariable("talhao", op.getSafraTalhao().getTalhao().getNome());
        context.setVariable("data", op.getDataExecucao());
        context.setVariable("area", op.getAreaTrabalhada());
        context.setVariable("custoTotal", op.getCustoTotal());

        context.setVariable("operacao",
                op.getOperacao() != null ? op.getOperacao().getOperacao() : "");

        context.setVariable("produtos", produtos);
        context.setVariable("combustiveis", combustiveis);
        context.setVariable("maquinas", maquinas);

        context.setVariable("totalProdutos", totalProdutos);
        context.setVariable("totalCombustivel", totalCombustivel);
        context.setVariable("totalMaquinas", totalMaquinas);

        /* ================= PDF ================= */

        String html = templateEngine.process("ordem-servico", context);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.withHtmlContent(html, null);
            builder.toStream(out);
            builder.run();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF", e);
        }
    }
}