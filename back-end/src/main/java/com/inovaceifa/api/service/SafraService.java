package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.safra.SafraCreateDTO;
import com.inovaceifa.api.dto.safra.SafraResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Fazenda;
import com.inovaceifa.api.model.Safra;
import com.inovaceifa.api.repository.SafraRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SafraService extends BaseCrudService<Safra, Long> {

    private final SafraRepository safraRepository;
    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<Safra, Long> getRepository() {
        return safraRepository;
    }

    /* =========================================================
       VALIDAR ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(Safra safra) {

        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();

        if (!safra.getFazenda().getId().equals(fazendaAtiva.getId())) {
            throw new AuthException("Safra não pertence à fazenda ativa");
        }
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public PageResponseDTO<SafraResponseDTO> listar(Pageable pageable) {

        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                safraRepository.findByFazendaId(fazendaAtiva.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public SafraResponseDTO buscarPorId(Long id) {

        Safra safra = super.buscarEntity(id);

        return toResponseDTO(safra);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public SafraResponseDTO criarSafra(SafraCreateDTO dto) {

        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();

        if (dto.getDataFinal().isBefore(dto.getDataInicial())) {
            throw new AuthException("Data final não pode ser anterior à data inicial");
        }

        Safra safra = new Safra();

        safra.setFazenda(fazendaAtiva);
        safra.setNome(dto.getNome());
        safra.setDataInicial(dto.getDataInicial());
        safra.setDataFinal(dto.getDataFinal());

        safra.setAreaPlantada(dto.getAreaPlantada());
        safra.setOrcamentoPrevisto(dto.getOrcamentoPrevisto());

        safra = super.salvarEntity(safra);

        return toResponseDTO(safra);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public SafraResponseDTO atualizar(Long id, SafraCreateDTO dto) {

        Safra safra = super.buscarEntity(id);

        if (dto.getDataFinal().isBefore(dto.getDataInicial())) {
            throw new AuthException("Data final não pode ser anterior à data inicial");
        }

        safra.setNome(dto.getNome());
        safra.setDataInicial(dto.getDataInicial());
        safra.setDataFinal(dto.getDataFinal());

        safra.setAreaPlantada(dto.getAreaPlantada());
        safra.setOrcamentoPrevisto(dto.getOrcamentoPrevisto());

        safra = super.salvarEntity(safra);

        return toResponseDTO(safra);
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    public void excluir(Long id) {

        Safra safra = super.buscarEntity(id);

        safraRepository.delete(safra);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private SafraResponseDTO toResponseDTO(Safra s) {

        return SafraResponseDTO.builder()
                .id(s.getId())
                .nome(s.getNome())
                .dataInicial(s.getDataInicial())
                .dataFinal(s.getDataFinal())
                .areaPlantada(s.getAreaPlantada())
                .orcamentoPrevisto(s.getOrcamentoPrevisto())
                .build();
    }
}