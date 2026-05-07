package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.folhapagamento.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FolhaPagamentoService {

    private final FolhaPagamentoRepository repository;
    private final FuncionarioRepository funcionarioRepository;
    private final ContextoFazendaService contexto;
    private final ParametroService parametroService;

    /* ========================= LISTAR ATIVOS ========================= */

    public PageResponseDTO<FolhaPagamentoResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoTrue(
                        contexto.getProprietario().getId(),
                        contexto.getFazendaAtiva().getId(),
                        contexto.getSafraAtiva().getId(),
                        pageable
                ),
                this::toDTO
        );
    }

    /* ========================= LISTAR INATIVOS ========================= */

    public PageResponseDTO<FolhaPagamentoResponseDTO> listarInativos(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_IdAndAtivoFalse(
                        contexto.getProprietario().getId(),
                        contexto.getFazendaAtiva().getId(),
                        contexto.getSafraAtiva().getId(),
                        pageable
                ),
                this::toDTO
        );
    }

    /* ========================= BUSCAR ========================= */

    public FolhaPagamentoResponseDTO buscar(Long id) {

        FolhaPagamento f = repository.findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        return toDTO(f);
    }

    /* ========================= CRIAR ========================= */

    @Transactional
    public FolhaPagamentoResponseDTO criar(FolhaPagamentoCreateDTO dto) {

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new AuthException("Funcionário não encontrado"));

        BigDecimal salarioBase = dto.getSalarioBase() != null
                ? dto.getSalarioBase()
                : BigDecimal.ZERO;

        /* 🔥 BUSCAR PARÂMETRO */
        BigDecimal percentual = parametroService.getPercentualEncargos();

        if (percentual == null || percentual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Parâmetro PERCENTUAL_ENCARGOS não configurado");
        }

        /* 🔥 CÁLCULO (IGUAL EXCEL) */
        BigDecimal encargos = salarioBase
                .multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = salarioBase
                .add(encargos)
                .setScale(2, RoundingMode.HALF_UP);

        FolhaPagamento f = new FolhaPagamento();

        f.setFuncionario(funcionario);
        f.setMesAno(dto.getMesAno());
        f.setSalarioBase(salarioBase);
        f.setEncargos(encargos);
        f.setTotal(total);

        f.setProprietario(contexto.getProprietario());
        f.setFazenda(contexto.getFazendaAtiva());
        f.setSafra(contexto.getSafraAtiva());
        f.setAtivo(true);

        f = repository.save(f);

        return toDTO(f);
    }

    /* ========================= ATUALIZAR ========================= */

    @Transactional
    public FolhaPagamentoResponseDTO atualizar(Long id, FolhaPagamentoUpdateDTO dto) {

        FolhaPagamento f = repository.findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        if (dto.getSalarioBase() != null) {
            f.setSalarioBase(dto.getSalarioBase());
        }

        BigDecimal salarioBase = f.getSalarioBase() != null
                ? f.getSalarioBase()
                : BigDecimal.ZERO;

        /* 🔥 BUSCAR PARÂMETRO */
        BigDecimal percentual = parametroService.getPercentualEncargos();

        if (percentual == null || percentual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Parâmetro PERCENTUAL_ENCARGOS não configurado");
        }

        /* 🔥 CÁLCULO (IGUAL EXCEL) */
        BigDecimal encargos = salarioBase
                .multiply(percentual)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = salarioBase
                .add(encargos)
                .setScale(2, RoundingMode.HALF_UP);

        f.setEncargos(encargos);
        f.setTotal(total);

        f = repository.save(f);

        return toDTO(f);
    }

    /* ========================= INATIVAR ========================= */

    @Transactional
    public void excluir(Long id) {

        FolhaPagamento f = repository.findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        f.setAtivo(false);

        repository.save(f);
    }

    /* ========================= REATIVAR ========================= */

    @Transactional
    public void reativar(Long id) {

        FolhaPagamento f = repository.findById(id)
                .orElseThrow(() -> new AuthException("Registro não encontrado"));

        f.setAtivo(true);

        repository.save(f);
    }

    /* ========================= DTO ========================= */

    private FolhaPagamentoResponseDTO toDTO(FolhaPagamento f) {

        return FolhaPagamentoResponseDTO.builder()
                .id(f.getId())
                .funcionarioId(f.getFuncionario().getId())
                .funcionarioNome(f.getFuncionario().getNome())
                .mesAno(f.getMesAno())
                .salarioBase(f.getSalarioBase())
                .encargos(f.getEncargos())
                .total(f.getTotal())
                .ativo(f.getAtivo())
                .build();
    }
}