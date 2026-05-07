package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.produto.ProdutoResponseDTO;
import com.inovaceifa.api.dto.produto.ProdutoCreateDTO;
import com.inovaceifa.api.dto.produto.ProdutoUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Fazenda;
import com.inovaceifa.api.model.Produto;
import com.inovaceifa.api.model.RefGrupo;
import com.inovaceifa.api.model.RefFamilia;
import com.inovaceifa.api.repository.ProdutoRepository;
import com.inovaceifa.api.repository.RefGrupoRepository;
import com.inovaceifa.api.repository.RefFamiliaRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService extends BaseCrudService<Produto, Long> {

    private final ProdutoRepository produtoRepository;
    private final RefGrupoRepository grupoRepository;
    private final RefFamiliaRepository familiaRepository;
    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<Produto, Long> getRepository() {
        return produtoRepository;
    }

    /* =========================================================
       VALIDAR ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(Produto produto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!produto.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Acesso negado ao produto");
        }
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public List<ProdutoResponseDTO> listar() {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return produtoRepository
                .findByFazendaIdAndAtivoTrue(fazenda.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PageResponseDTO<ProdutoResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                produtoRepository.findByFazendaIdAndAtivoTrue(
                        fazenda.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public List<ProdutoResponseDTO> listarInativos() {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return produtoRepository
                .findByFazendaIdAndAtivoFalse(fazenda.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public PageResponseDTO<ProdutoResponseDTO> listarInativos(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                produtoRepository.findByFazendaIdAndAtivoFalse(
                        fazenda.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = super.buscarEntity(id);

        return toResponseDTO(produto);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public ProdutoResponseDTO criar(ProdutoCreateDTO dto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        RefGrupo grupo = grupoRepository.findById(dto.getGrupoId())
                .orElseThrow(() -> new AuthException("Grupo não encontrado"));

        RefFamilia familia = familiaRepository.findById(dto.getFamiliaId())
                .orElseThrow(() -> new AuthException("Família não encontrada"));

        Produto produto = new Produto();

        produto.setNome(dto.getNome());
        produto.setCodigo(dto.getCodigo());
        produto.setUnidade(dto.getUnidade());
        produto.setAtivoNutr(dto.getAtivoNutr());
        produto.setGrupo(grupo);
        produto.setFamilia(familia);
        produto.setFazenda(fazenda);
        produto.setAtivo(true);

        produto.setQtde(dto.getQtde() != null ? dto.getQtde() : BigDecimal.ZERO);
        produto.setVlrUnitario(dto.getVlrUnitario() != null ? dto.getVlrUnitario() : BigDecimal.ZERO);
        produto.setVlrTotal(produto.getQtde().multiply(produto.getVlrUnitario()));

        // 🔥 ADICIONADO
        produto.setPrecoCusto(dto.getPrecoCusto());

        produto = super.salvarEntity(produto);

        return toResponseDTO(produto);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public ProdutoResponseDTO atualizar(Long id, ProdutoUpdateDTO dto) {

        Produto produto = super.buscarEntity(id);

        if (dto.getNome() != null) produto.setNome(dto.getNome());
        if (dto.getCodigo() != null) produto.setCodigo(dto.getCodigo());
        if (dto.getUnidade() != null) produto.setUnidade(dto.getUnidade());
        if (dto.getAtivoNutr() != null) produto.setAtivoNutr(dto.getAtivoNutr());

        if (dto.getGrupoId() != null) {

            RefGrupo grupo = grupoRepository.findById(dto.getGrupoId())
                    .orElseThrow(() -> new AuthException("Grupo não encontrado"));

            produto.setGrupo(grupo);
        }

        if (dto.getFamiliaId() != null) {

            RefFamilia familia = familiaRepository.findById(dto.getFamiliaId())
                    .orElseThrow(() -> new AuthException("Família não encontrada"));

            produto.setFamilia(familia);
        }

        if (dto.getQtde() != null) produto.setQtde(dto.getQtde());
        if (dto.getVlrUnitario() != null) produto.setVlrUnitario(dto.getVlrUnitario());

        // 🔥 ADICIONADO
        if (dto.getPrecoCusto() != null)
            produto.setPrecoCusto(dto.getPrecoCusto());

        produto.setVlrTotal(produto.getQtde().multiply(produto.getVlrUnitario()));

        produto = super.salvarEntity(produto);

        return toResponseDTO(produto);
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    public void excluir(Long id) {

        Produto produto = super.buscarEntity(id);

        produto.setAtivo(false);

        super.salvarEntity(produto);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    public void reativar(Long id) {

        Produto produto = super.buscarEntity(id);

        produto.setAtivo(true);

        super.salvarEntity(produto);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private ProdutoResponseDTO toResponseDTO(Produto produto) {

        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .codigo(produto.getCodigo())
                .unidade(produto.getUnidade())
                .ativoNutr(produto.getAtivoNutr())
                .qtde(produto.getQtde())
                .vlrUnitario(produto.getVlrUnitario())
                .vlrTotal(produto.getVlrTotal())
                // 🔥 ADICIONADO
                .precoCusto(produto.getPrecoCusto())
                .grupoId(produto.getGrupo().getId())
                .grupoDescricao(produto.getGrupo().getDescricao())
                .familiaId(produto.getFamilia().getId())
                .familiaDescricao(produto.getFamilia().getDescricao())
                .ativo(produto.getAtivo())
                .fazendaId(produto.getFazenda().getId())
                .build();
    }
}