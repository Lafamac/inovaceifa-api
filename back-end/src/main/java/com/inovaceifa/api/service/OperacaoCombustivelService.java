package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.operacaocombustivel.*;
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
public class OperacaoCombustivelService extends BaseCrudService<OperacaoCombustivel, Long> {

    private final OperacaoCombustivelRepository repository;
    private final OperacaoTalhaoRepository operacaoTalhaoRepository;
    private final MaquinaRepository maquinaRepository;

    // 🔥 NOVO
    private final OperacaoTalhaoService operacaoTalhaoService;

    private final ContextoFazendaService contexto;

    @Override
    protected JpaRepository<OperacaoCombustivel, Long> getRepository() {
        return repository;
    }

    @Override
    protected void validarAcesso(OperacaoCombustivel entity) {

        if (!entity.getFazenda().getId().equals(contexto.getFazendaAtiva().getId())) {
            throw new AuthException("Combustível não pertence à fazenda ativa");
        }
    }

    public PageResponseDTO<OperacaoCombustivelResponseDTO> listar(Pageable pageable) {

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

    public OperacaoCombustivelResponseDTO criar(OperacaoCombustivelCreateDTO dto) {

        OperacaoTalhao operacao = operacaoTalhaoRepository.findById(dto.getOperacaoTalhaoId())
                .orElseThrow(() -> new AuthException("Operação não encontrada"));

        Maquina maquina = maquinaRepository.findById(dto.getMaquinaId())
                .orElseThrow(() -> new AuthException("Máquina não encontrada"));

        OperacaoCombustivel oc = new OperacaoCombustivel();

        oc.setOperacaoTalhao(operacao);
        oc.setMaquina(maquina);
        oc.setLitros(dto.getLitros());
        oc.setValorUnitario(dto.getValorUnitario());

        oc.setProprietario(contexto.getProprietario());
        oc.setFazenda(contexto.getFazendaAtiva());
        oc.setSafra(contexto.getSafraAtiva());

        oc = super.salvarEntity(oc);

        // 🔥 RECALCULAR
        operacaoTalhaoService.recalcularCusto(dto.getOperacaoTalhaoId());

        return toResponse(oc);
    }

    private OperacaoCombustivelResponseDTO toResponse(OperacaoCombustivel oc) {

        return OperacaoCombustivelResponseDTO.builder()
                .id(oc.getId())
                .operacaoTalhaoId(oc.getOperacaoTalhao().getId())
                .maquinaId(oc.getMaquina().getId())
                .maquinaNome(oc.getMaquina().getNome())
                .litros(oc.getLitros())
                .valorUnitario(oc.getValorUnitario())
                .build();
    }
}