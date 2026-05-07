package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.operacao.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.model.CadastroOperacao;
import com.inovaceifa.api.repository.CadastroOperacaoRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CadastroOperacaoService extends BaseCrudService<CadastroOperacao, Long> {

    private final CadastroOperacaoRepository repository;

    @Override
    protected JpaRepository<CadastroOperacao, Long> getRepository() {
        return repository;
    }

    /* =========================================================
       LISTAR
       ========================================================= */

    public PageResponseDTO<CadastroOperacaoResponseDTO> listar(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findAll(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<CadastroOperacaoResponseDTO> listarAtivos(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findAllByAtivoTrue(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<CadastroOperacaoResponseDTO> listarInativos(Pageable pageable) {

        return PageUtils.toPageResponse(
                repository.findAllByAtivoFalse(pageable),
                this::toDTO
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public CadastroOperacaoResponseDTO buscar(Long id) {

        CadastroOperacao entity = super.buscarEntity(id);

        return toDTO(entity);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public CadastroOperacaoResponseDTO criar(CadastroOperacaoCreateDTO dto) {

        CadastroOperacao entity = new CadastroOperacao();

        entity.setCodOper(dto.getCodOper());
        entity.setCultura(dto.getCultura());
        entity.setOperacao(dto.getOperacao());
        entity.setModalidade(dto.getModalidade());
        entity.setDeslocamento(dto.getDeslocamento());
        entity.setAtividade(dto.getAtividade());
        entity.setFaixaNominal(dto.getFaixaNominal());
        entity.setVelocidadeOp(dto.getVelocidadeOp());
        entity.setEficienciaCampo(dto.getEficienciaCampo());
        entity.setGastoDiesel(dto.getGastoDiesel());
        entity.setAtivo(true);

        entity = super.salvarEntity(entity);

        return toDTO(entity);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public CadastroOperacaoResponseDTO atualizar(Long id, CadastroOperacaoUpdateDTO dto) {

        CadastroOperacao entity = super.buscarEntity(id);

        entity.setCodOper(dto.getCodOper());
        entity.setCultura(dto.getCultura());
        entity.setOperacao(dto.getOperacao());
        entity.setModalidade(dto.getModalidade());
        entity.setDeslocamento(dto.getDeslocamento());
        entity.setAtividade(dto.getAtividade());
        entity.setFaixaNominal(dto.getFaixaNominal());
        entity.setVelocidadeOp(dto.getVelocidadeOp());
        entity.setEficienciaCampo(dto.getEficienciaCampo());
        entity.setGastoDiesel(dto.getGastoDiesel());

        entity = super.salvarEntity(entity);

        return toDTO(entity);
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    public void desativar(Long id) {

        CadastroOperacao entity = super.buscarEntity(id);

        entity.setAtivo(false);

        super.salvarEntity(entity);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    public void reativar(Long id) {

        CadastroOperacao entity = super.buscarEntity(id);

        entity.setAtivo(true);

        super.salvarEntity(entity);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private CadastroOperacaoResponseDTO toDTO(CadastroOperacao entity) {

        return CadastroOperacaoResponseDTO.builder()
                .id(entity.getId())
                .codOper(entity.getCodOper())
                .cultura(entity.getCultura())
                .operacao(entity.getOperacao())
                .modalidade(entity.getModalidade())
                .deslocamento(entity.getDeslocamento())
                .atividade(entity.getAtividade())
                .faixaNominal(entity.getFaixaNominal())
                .velocidadeOp(entity.getVelocidadeOp())
                .eficienciaCampo(entity.getEficienciaCampo())
                .gastoDiesel(entity.getGastoDiesel())
                .ativo(entity.getAtivo())
                .build();
    }

}