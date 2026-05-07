package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.talhao.TalhaoCreateDTO;
import com.inovaceifa.api.dto.talhao.TalhaoResponseDTO;
import com.inovaceifa.api.dto.talhao.TalhaoUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TalhaoService extends BaseCrudService<Talhao, Long> {

    private final TalhaoRepository talhaoRepository;

    // 🔥 REMOVIDO uso de cultura
    private final RefResFerrugemRepository resFerrugemRepository;
    private final RefStCultivoRepository stCultivoRepository;

    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<Talhao, Long> getRepository() {
        return talhaoRepository;
    }

    /* =========================================================
       VALIDAÇÃO AUTOMÁTICA DE ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(Talhao t) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!t.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Talhão não pertence à fazenda ativa");
        }
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<TalhaoResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                talhaoRepository.findByFazendaIdAndAtivoTrue(fazenda.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<TalhaoResponseDTO> listarInativos(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                talhaoRepository.findByFazendaIdAndAtivoFalse(fazenda.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public TalhaoResponseDTO buscarPorId(Long id) {
        return toResponseDTO(super.buscarEntity(id));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public TalhaoResponseDTO criarTalhao(TalhaoCreateDTO dto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        Talhao t = new Talhao();

        aplicarDados(t, dto, fazenda);

        t.setDataCriacao(LocalDateTime.now());
        t.setAtivo(true);

        t = super.salvarEntity(t);

        return toResponseDTO(t);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public TalhaoResponseDTO atualizar(Long id, TalhaoUpdateDTO dto) {

        Talhao t = super.buscarEntity(id);

        aplicarDadosUpdate(t, dto);

        t = super.salvarEntity(t);

        return toResponseDTO(t);
    }

    /* =========================================================
       SOFT DELETE
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        Talhao t = super.buscarEntity(id);

        t.setAtivo(false);

        super.salvarEntity(t);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        Talhao t = super.buscarEntity(id);

        t.setAtivo(true);

        super.salvarEntity(t);
    }

    /* =========================================================
       HELPERS (AJUSTADOS MINIMAMENTE)
       ========================================================= */

    private void aplicarDados(Talhao t, TalhaoCreateDTO dto, Fazenda fazenda) {

        t.setNome(dto.getNome());
        t.setFazenda(fazenda);
        t.setArea(dto.getArea());
        t.setEspacamentoRua(dto.getEspacamentoRua());
        t.setEspacamentoPlanta(dto.getEspacamentoPlanta());
        t.setMaterial(dto.getMaterial());

        // 🔥 agora opcionais
        if (dto.getResistenciaFerrugemId() != null) {
            t.setResistenciaFerrugem(
                    resFerrugemRepository.findById(dto.getResistenciaFerrugemId())
                            .orElseThrow(() -> new AuthException("Resistência inválida"))
            );
        }

        if (dto.getSistemaCultivoId() != null) {
            t.setSistemaCultivo(
                    stCultivoRepository.findById(dto.getSistemaCultivoId())
                            .orElseThrow(() -> new AuthException("Sistema de cultivo inválido"))
            );
        }
    }

    private void aplicarDadosUpdate(Talhao t, TalhaoUpdateDTO dto) {

        t.setArea(dto.getArea());
        t.setEspacamentoRua(dto.getEspacamentoRua());
        t.setEspacamentoPlanta(dto.getEspacamentoPlanta());
        t.setMaterial(dto.getMaterial());

        if (dto.getResistenciaFerrugemId() != null) {
            t.setResistenciaFerrugem(
                    resFerrugemRepository.findById(dto.getResistenciaFerrugemId())
                            .orElseThrow(() -> new AuthException("Resistência inválida"))
            );
        }

        if (dto.getSistemaCultivoId() != null) {
            t.setSistemaCultivo(
                    stCultivoRepository.findById(dto.getSistemaCultivoId())
                            .orElseThrow(() -> new AuthException("Sistema inválido"))
            );
        }
    }

    private TalhaoResponseDTO toResponseDTO(Talhao t) {

        return TalhaoResponseDTO.builder()
                .id(t.getId())
                .nome(t.getNome())
                .fazendaId(t.getFazenda().getId())
                .dataCriacao(t.getDataCriacao())
                .area(t.getArea())
                .espacamentoRua(t.getEspacamentoRua())
                .espacamentoPlanta(t.getEspacamentoPlanta())
                .material(t.getMaterial())
                .resistenciaFerrugemId(
                        t.getResistenciaFerrugem() != null ? t.getResistenciaFerrugem().getId() : null
                )
                .sistemaCultivoId(
                        t.getSistemaCultivo() != null ? t.getSistemaCultivo().getId() : null
                )
                .ativo(t.getAtivo())
                .build();
    }
}