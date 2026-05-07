package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.operacaofuncionario.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OperacaoFuncionarioService extends BaseCrudService<OperacaoFuncionario, Long> {

    private final OperacaoFuncionarioRepository repository;
    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final FuncionarioRepository funcionarioRepository;

    /* 🔥 NOVO */
    private final OperacaoTalhaoService operacaoTalhaoService;

    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<OperacaoFuncionario, Long> getRepository() {
        return repository;
    }

    @Override
    protected void validarAcesso(OperacaoFuncionario entity) {

        if (!entity.getFazenda().getId().equals(contexto.getFazendaAtiva().getId())) {
            throw new AuthException("Funcionário da operação não pertence à fazenda ativa");
        }
    }

    public PageResponseDTO<OperacaoFuncionarioResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findByProprietario_IdAndFazenda_IdAndSafra_Id(
                        contexto.getProprietario().getId(),
                        contexto.getFazendaAtiva().getId(),
                        contexto.getSafraAtiva().getId(),
                        pageable
                ),
                this::toResponse
        );
    }

    public OperacaoFuncionarioResponseDTO criar(OperacaoFuncionarioCreateDTO dto) {

        OperacaoTalhao operacao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        Funcionario funcionario = funcionarioRepository.findById(dto.getFuncionarioId())
                .orElseThrow(() -> new AuthException("Funcionário não encontrado"));

        OperacaoFuncionario of = new OperacaoFuncionario();

        of.setOperacaoTalhao(operacao);
        of.setFuncionario(funcionario);
        of.setHorasTrabalhadas(dto.getHorasTrabalhadas());

        of.setProprietario(contexto.getProprietario());
        of.setFazenda(contexto.getFazendaAtiva());
        of.setSafra(contexto.getSafraAtiva());

        of = super.salvarEntity(of);

        /* 🔥 RECALCULAR CUSTO DA OPERAÇÃO */
        operacaoTalhaoService.recalcularCusto(dto.getOperacaoTalhaoId());

        return toResponse(of);
    }

    private OperacaoFuncionarioResponseDTO toResponse(OperacaoFuncionario of) {

        return OperacaoFuncionarioResponseDTO.builder()
                .id(of.getId())
                .funcionarioId(of.getFuncionario().getId())
                .funcionarioNome(of.getFuncionario().getNome())
                .horasTrabalhadas(of.getHorasTrabalhadas())
                .build();
    }
}