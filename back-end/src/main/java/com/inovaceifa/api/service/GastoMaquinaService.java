package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.gastomaquina.GastoMaquinaResponseDTO;
import com.inovaceifa.api.dto.gastomaquina.GastoMaquinaCreateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.GastoMaquinaRepository;
import com.inovaceifa.api.repository.MaquinaRepository;
import com.inovaceifa.api.repository.RefTipoGastoMaquinaRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoMaquinaService extends BaseCrudService<GastoMaquina, Long> {

    private final GastoMaquinaRepository gastoMaquinaRepository;
    private final MaquinaRepository maquinaRepository;
    private final RefTipoGastoMaquinaRepository refTipoGastoMaquinaRepository;
    private final FuncionarioService funcionarioService;
    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<GastoMaquina, Long> getRepository() {
        return gastoMaquinaRepository;
    }

    /* =========================================================
       VALIDAR ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(GastoMaquina gasto) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!gasto.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Acesso negado ao gasto");
        }
    }

    /* =========================================================
       LISTAR (LEGADO)
       ========================================================= */

    public List<GastoMaquinaResponseDTO> listar() {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return gastoMaquinaRepository
                .findByFazendaIdAndSafraId(fazenda.getId(), safra.getId())
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================================================
       LISTAR (PAGINADO)
       ========================================================= */

    public PageResponseDTO<GastoMaquinaResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        return PageUtils.toPageResponse(
                gastoMaquinaRepository.findByFazendaIdAndSafraId(
                        fazenda.getId(),
                        safra.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Transactional
    public GastoMaquinaResponseDTO criar(GastoMaquinaCreateDTO dto) {

        validarDados(dto);

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();
        Safra safra = contextoFazendaService.getSafraAtiva();

        Maquina maquina = buscarMaquinaValida(dto.getMaquinaId(), fazenda);
        RefTipoGastoMaquina tipoGasto = buscarTipoGasto(dto.getTipoGastoId());

        Funcionario funcionario = null;

        if (dto.getFuncionarioId() != null) {
            funcionario = funcionarioService.buscarPorId(dto.getFuncionarioId());
        }

        GastoMaquina gasto = new GastoMaquina();

        gasto.setData(dto.getData());
        gasto.setDescricao(dto.getDescricao());
        gasto.setValor(dto.getValor());
        gasto.setTipoGasto(tipoGasto);
        gasto.setMaquina(maquina);
        gasto.setFazenda(fazenda);
        gasto.setSafra(safra);
        gasto.setFuncionario(funcionario);

        gasto = super.salvarEntity(gasto);

        return toResponseDTO(gasto);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Transactional
    public GastoMaquinaResponseDTO atualizar(Long id, GastoMaquinaCreateDTO dto) {

        validarDados(dto);

        GastoMaquina gasto = super.buscarEntity(id);

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        Maquina maquina = buscarMaquinaValida(dto.getMaquinaId(), fazenda);
        RefTipoGastoMaquina tipoGasto = buscarTipoGasto(dto.getTipoGastoId());

        Funcionario funcionario = null;

        if (dto.getFuncionarioId() != null) {
            funcionario = funcionarioService.buscarPorId(dto.getFuncionarioId());
        }

        gasto.setData(dto.getData());
        gasto.setDescricao(dto.getDescricao());
        gasto.setValor(dto.getValor());
        gasto.setTipoGasto(tipoGasto);
        gasto.setMaquina(maquina);
        gasto.setFuncionario(funcionario);

        gasto = super.salvarEntity(gasto);

        return toResponseDTO(gasto);
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        GastoMaquina gasto = super.buscarEntity(id);

        gastoMaquinaRepository.delete(gasto);
    }

    /* =========================================================
       MÉTODOS PRIVADOS
       ========================================================= */

    private void validarDados(GastoMaquinaCreateDTO dto) {

        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new AuthException("Valor do gasto deve ser maior que zero");
        }

        if (dto.getData() == null || dto.getData().isAfter(LocalDate.now())) {
            throw new AuthException("Data do gasto inválida");
        }
    }

    private Maquina buscarMaquinaValida(Long maquinaId, Fazenda fazenda) {

        Maquina maquina = maquinaRepository.findById(maquinaId)
                .orElseThrow(() -> new AuthException("Máquina não encontrada"));

        if (!maquina.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Máquina não pertence à fazenda ativa");
        }

        return maquina;
    }

    private RefTipoGastoMaquina buscarTipoGasto(Long tipoGastoId) {

        return refTipoGastoMaquinaRepository.findById(tipoGastoId)
                .orElseThrow(() -> new AuthException("Tipo de gasto inválido"));
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private GastoMaquinaResponseDTO toResponseDTO(GastoMaquina g) {

        return GastoMaquinaResponseDTO.builder()
                .id(g.getId())
                .data(g.getData())
                .descricao(g.getDescricao())
                .valor(g.getValor())
                .tipoGastoId(g.getTipoGasto() != null ? g.getTipoGasto().getId() : null)
                .tipoGastoDescricao(g.getTipoGasto() != null ? g.getTipoGasto().getDescricao() : null)
                .maquinaId(g.getMaquina() != null ? g.getMaquina().getId() : null)
                .maquinaNome(g.getMaquina() != null ? g.getMaquina().getNome() : null)
                .fazendaId(g.getFazenda() != null ? g.getFazenda().getId() : null)
                .safraId(g.getSafra() != null ? g.getSafra().getId() : null)
                .funcionarioId(g.getFuncionario() != null ? g.getFuncionario().getId() : null)
                .build();
    }
}