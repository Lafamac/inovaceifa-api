package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.administrativo.AdministrativoCreateDTO;
import com.inovaceifa.api.dto.administrativo.AdministrativoUpdateDTO;
import com.inovaceifa.api.dto.administrativo.AdministrativoResponseDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.mapper.PageMapper;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.AdministrativoRepository;
import com.inovaceifa.api.repository.ContaGerencialRepository;
import com.inovaceifa.api.repository.RefDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdministrativoService {

    private final AdministrativoRepository administrativoRepository;
    private final ContaGerencialRepository contaGerencialRepository;
    private final RefDespesaRepository refDespesaRepository;
    private final ContextoFazendaService contextoFazendaService;

    /* =========================================================
       LISTAR
    ========================================================= */

    public PageResponseDTO<AdministrativoResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        Page<AdministrativoResponseDTO> page =
                administrativoRepository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId(),
                        pageable
                ).map(this::toResponseDTO);

        return PageMapper.toPageResponse(page);
    }

    /* =========================================================
       CRIAR
    ========================================================= */

    @Transactional
    public AdministrativoResponseDTO criar(AdministrativoCreateDTO dto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        ContaGerencial conta = contaGerencialRepository
                .findById(dto.getContaGerencialId().longValue())
                .orElseThrow(() -> new AuthException("Conta gerencial inválida"));

        RefDespesa despesa = refDespesaRepository
                .findById(dto.getDespesaEducampoId().longValue())
                .orElseThrow(() -> new AuthException("Despesa Educampo inválida"));

        Administrativo a = new Administrativo();

        a.setDescricao(dto.getDescricao());
        a.setMesAno(dto.getMesAno());
        a.setUn(dto.getUn());

        a.setContaGerencial(conta);
        a.setDespesaEducampo(despesa);

        a.setFazenda(fazenda);
        a.setSafra(safra);

        a.setValorUnitPlanejado(dto.getValorUnitPlanejado());
        a.setQuantidadePlanejada(dto.getQuantidadePlanejada());
        a.setValorTotalPlanejado(dto.getValorTotalPlanejado());
        a.setValorHaPlanejado(dto.getValorHaPlanejado());

        a.setValorUnitRealizado(dto.getValorUnitRealizado());
        a.setQuantidadeRealizada(dto.getQuantidadeRealizada());
        a.setValorTotalRealizado(dto.getValorTotalRealizado());
        a.setValorHaRealizado(dto.getValorHaRealizado());

        return toResponseDTO(administrativoRepository.save(a));
    }

    /* =========================================================
       ATUALIZAR
    ========================================================= */

    @Transactional
    public AdministrativoResponseDTO atualizar(Long id, AdministrativoUpdateDTO dto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        Administrativo a = administrativoRepository
                .findByIdAndFazendaIdAndSafraId(id, fazenda.getId(), safra.getId())
                .orElseThrow(() -> new AuthException("Registro administrativo não encontrado"));

        if (dto.getContaGerencialId() != null) {

            a.setContaGerencial(
                    contaGerencialRepository
                            .findById(dto.getContaGerencialId().longValue())
                            .orElseThrow(() -> new AuthException("Conta gerencial inválida"))
            );

        }

        if (dto.getDespesaEducampoId() != null) {

            a.setDespesaEducampo(
                    refDespesaRepository
                            .findById(dto.getDespesaEducampoId().longValue())
                            .orElseThrow(() -> new AuthException("Despesa Educampo inválida"))
            );

        }

        if (dto.getDescricao() != null) a.setDescricao(dto.getDescricao());
        if (dto.getMesAno() != null) a.setMesAno(dto.getMesAno());
        if (dto.getUn() != null) a.setUn(dto.getUn());

        if (dto.getValorUnitPlanejado() != null) a.setValorUnitPlanejado(dto.getValorUnitPlanejado());
        if (dto.getQuantidadePlanejada() != null) a.setQuantidadePlanejada(dto.getQuantidadePlanejada());
        if (dto.getValorTotalPlanejado() != null) a.setValorTotalPlanejado(dto.getValorTotalPlanejado());
        if (dto.getValorHaPlanejado() != null) a.setValorHaPlanejado(dto.getValorHaPlanejado());

        if (dto.getValorUnitRealizado() != null) a.setValorUnitRealizado(dto.getValorUnitRealizado());
        if (dto.getQuantidadeRealizada() != null) a.setQuantidadeRealizada(dto.getQuantidadeRealizada());
        if (dto.getValorTotalRealizado() != null) a.setValorTotalRealizado(dto.getValorTotalRealizado());
        if (dto.getValorHaRealizado() != null) a.setValorHaRealizado(dto.getValorHaRealizado());

        return toResponseDTO(administrativoRepository.save(a));
    }

     /* =========================================================
       MAPPER
    ========================================================= */

    private AdministrativoResponseDTO toResponseDTO(Administrativo a) {

        return AdministrativoResponseDTO.builder()
                .id(a.getId())
                .descricao(a.getDescricao())
                .mesAno(a.getMesAno())
                .un(a.getUn())

                .fazendaId(a.getFazenda().getId())
                .safraId(a.getSafra().getId())

                .contaGerencialId(a.getContaGerencial().getId())
                .contaGerencialDescricao(a.getContaGerencial().getDescricao())

                .despesaEducampoId(a.getDespesaEducampo().getId())
                .despesaEducampoDescricao(a.getDespesaEducampo().getDescricao())

                .valorUnitPlanejado(a.getValorUnitPlanejado())
                .quantidadePlanejada(a.getQuantidadePlanejada())
                .valorTotalPlanejado(a.getValorTotalPlanejado())
                .valorHaPlanejado(a.getValorHaPlanejado())

                .valorUnitRealizado(a.getValorUnitRealizado())
                .quantidadeRealizada(a.getQuantidadeRealizada())
                .valorTotalRealizado(a.getValorTotalRealizado())
                .valorHaRealizado(a.getValorHaRealizado())

                .build();
    }

}