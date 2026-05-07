package com.inovaceifa.api.service;

import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdemServicoCalculoService {

    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final OperacaoProdutoRepository operacaoProdutoRepository;
    private final OperacaoFuncionarioRepository operacaoFuncionarioRepository;
    private final OperacaoCombustivelRepository operacaoCombustivelRepository;
    private final HoraMaquinaRepository horaMaquinaRepository;
    private final ApontamentoTurmaRepository apontamentoTurmaRepository;

    public BigDecimal calcularCustoInsumos(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordemServicoId);

        for (OperacaoTalhao talhao : talhoes) {

            List<OperacaoProduto> produtos =
                    operacaoProdutoRepository.findByOperacaoTalhao_Id(talhao.getId());

            for (OperacaoProduto p : produtos) {

                if (p.getProduto() == null || p.getQuantidade() == null) continue;

                BigDecimal custoUnitario = p.getProduto().getPrecoCusto();

                if (custoUnitario != null) {

                    BigDecimal custo =
                            custoUnitario.multiply(p.getQuantidade());

                    total = total.add(custo);

                } else if (p.getVlrTotal() != null) {

                    total = total.add(p.getVlrTotal());
                }
            }
        }

        return total;
    }

    public BigDecimal calcularCustoMaquinas(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<HoraMaquina> horas =
                horaMaquinaRepository.findByOperacaoTalhao_OrdemServico_Id(ordemServicoId);

        for (HoraMaquina h : horas) {

            if (h.getMaquina() == null) continue;

            BigDecimal valorHora = h.getMaquina().getValorDiaria();

            if (valorHora == null) continue;

            BigDecimal custo =
                    valorHora.multiply(h.getHorasTrabalhadas());

            total = total.add(custo);
        }

        return total;
    }

    public BigDecimal calcularCustoMaoObra(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordemServicoId);

        for (OperacaoTalhao talhao : talhoes) {

            List<OperacaoFuncionario> funcionarios =
                    operacaoFuncionarioRepository.findByOperacaoTalhao_Id(talhao.getId());

            for (OperacaoFuncionario f : funcionarios) {

                if (f.getFuncionario() == null) continue;

                BigDecimal salario = f.getFuncionario().getSalario();

                if (salario == null) continue;

                BigDecimal custoHora =
                        salario.divide(BigDecimal.valueOf(220), 6, RoundingMode.HALF_UP);

                BigDecimal custo =
                        custoHora.multiply(f.getHorasTrabalhadas());

                total = total.add(custo);
            }
        }

        return total;
    }

    public BigDecimal calcularCustoCombustivel(Long ordemServicoId, BigDecimal precoDiesel) {

        BigDecimal total = BigDecimal.ZERO;

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordemServicoId);

        for (OperacaoTalhao talhao : talhoes) {

            List<OperacaoCombustivel> combustiveis =
                    operacaoCombustivelRepository.findByOperacaoTalhao_Id(talhao.getId());

            for (OperacaoCombustivel c : combustiveis) {

                if (c.getLitros() == null) continue;

                BigDecimal custo =
                        precoDiesel.multiply(c.getLitros());

                total = total.add(custo);
            }
        }

        return total;
    }

    /* =========================
       🔥 NOVO MÉTODO
       ========================= */
    private BigDecimal calcularCustoTurma(Long ordemServicoId) {

        return apontamentoTurmaRepository.findByOrdemServicoId(ordemServicoId)
                .stream()
                .map(a -> a.getValorTotal() != null ? a.getValorTotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularAreaTotal(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordemServicoId);

        for (OperacaoTalhao t : talhoes) {

            if (t.getAreaTrabalhada() != null) {
                total = total.add(t.getAreaTrabalhada());
            }
        }

        return total;
    }

    public BigDecimal calcularHorasMaquina(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<HoraMaquina> horas =
                horaMaquinaRepository.findByOperacaoTalhao_OrdemServico_Id(ordemServicoId);

        for (HoraMaquina h : horas) {

            if (h.getHorasTrabalhadas() != null) {
                total = total.add(h.getHorasTrabalhadas());
            }
        }

        return total;
    }

    public BigDecimal calcularLitrosDiesel(Long ordemServicoId) {

        BigDecimal total = BigDecimal.ZERO;

        List<OperacaoTalhao> talhoes =
                operacaoTalhaoRepository.findByOrdemServico_Id(ordemServicoId);

        for (OperacaoTalhao talhao : talhoes) {

            List<OperacaoCombustivel> combustiveis =
                    operacaoCombustivelRepository.findByOperacaoTalhao_Id(talhao.getId());

            for (OperacaoCombustivel c : combustiveis) {

                if (c.getLitros() != null) {
                    total = total.add(c.getLitros());
                }
            }
        }

        return total;
    }

    public BigDecimal calcularCustoTotal(Long ordemServicoId, BigDecimal precoDiesel) {

        BigDecimal insumos = calcularCustoInsumos(ordemServicoId);
        BigDecimal maquinas = calcularCustoMaquinas(ordemServicoId);
        BigDecimal maoObra = calcularCustoMaoObra(ordemServicoId);
        BigDecimal combustivel = calcularCustoCombustivel(ordemServicoId, precoDiesel);

        // 🔥 NOVO
        BigDecimal custoTurma = calcularCustoTurma(ordemServicoId);

        return insumos
                .add(maquinas)
                .add(maoObra)
                .add(custoTurma) // 🔥 NOVO
                .add(combustivel);
    }

    public BigDecimal calcularCustoPorHectare(BigDecimal custoTotal, BigDecimal areaTotal) {

        if (areaTotal == null || areaTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return custoTotal.divide(areaTotal, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularConsumoDieselHectare(BigDecimal litros, BigDecimal areaTotal) {

        if (areaTotal == null || areaTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return litros.divide(areaTotal, 6, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularRendimentoOperacional(BigDecimal areaTotal, BigDecimal horasMaquina) {

        if (horasMaquina == null || horasMaquina.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return areaTotal.divide(horasMaquina, 6, RoundingMode.HALF_UP);
    }

    /* =========================================================
       🔥 NOVO MÉTODO (ATUALIZADO COM TURMA)
       ========================================================= */

    public ResultadoCalculoOS calcularResumoCompleto(Long ordemServicoId, BigDecimal precoDiesel) {

        BigDecimal insumos = calcularCustoInsumos(ordemServicoId);
        BigDecimal maquinas = calcularCustoMaquinas(ordemServicoId);
        BigDecimal maoObra = calcularCustoMaoObra(ordemServicoId);
        BigDecimal combustivel = calcularCustoCombustivel(ordemServicoId, precoDiesel);

        // 🔥 NOVO
        BigDecimal custoTurma = calcularCustoTurma(ordemServicoId);

        BigDecimal total = insumos
                .add(maquinas)
                .add(maoObra)
                .add(custoTurma) // 🔥 NOVO
                .add(combustivel);

        BigDecimal area = calcularAreaTotal(ordemServicoId);
        BigDecimal horas = calcularHorasMaquina(ordemServicoId);
        BigDecimal litros = calcularLitrosDiesel(ordemServicoId);

        ResultadoCalculoOS result = new ResultadoCalculoOS();

        result.setInsumos(insumos);
        result.setMaquinas(maquinas);
        result.setMaoObra(maoObra);
        result.setCombustivel(combustivel);
        result.setTotal(total);

        result.setAreaTotal(area);
        result.setHorasMaquina(horas);
        result.setLitrosDiesel(litros);

        return result;
    }
}