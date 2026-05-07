package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.maquina.MaquinaCreateDTO;
import com.inovaceifa.api.dto.maquina.MaquinaResponseDTO;
import com.inovaceifa.api.dto.maquina.MaquinaUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Fazenda;
import com.inovaceifa.api.model.Maquina;
import com.inovaceifa.api.model.Proprietario;
import com.inovaceifa.api.model.RefTipoMaquina;
import com.inovaceifa.api.model.RefTipoPosseMaquina;
import com.inovaceifa.api.repository.MaquinaRepository;
import com.inovaceifa.api.repository.RefTipoMaquinaRepository;
import com.inovaceifa.api.repository.RefTipoPosseMaquinaRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaquinaService extends BaseCrudService<Maquina, Long> {

    private final MaquinaRepository maquinaRepository;
    private final RefTipoMaquinaRepository refTipoMaquinaRepository;
    private final RefTipoPosseMaquinaRepository refTipoPosseMaquinaRepository;
    private final ContextoFazendaService contextoFazendaService;

    @Override
    protected JpaRepository<Maquina, Long> getRepository() {
        return maquinaRepository;
    }

    /* =========================================================
       VALIDAÇÃO AUTOMÁTICA DE ACESSO (BaseCrudService)
       ========================================================= */

    @Override
    protected void validarAcesso(Maquina maquina) {

        Proprietario proprietario = getProprietario();

        if (proprietario == null) {
            throw new AuthException("Proprietário não encontrado");
        }

        if (!maquina.getFazenda().getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Acesso negado à máquina");
        }
    }

    /* =========================================================
       NOVO CONTEXTO
       ========================================================= */

    private Proprietario getProprietario() {
        return contextoFazendaService.getProprietario();
    }

    private Fazenda getFazendaAtiva() {
        return contextoFazendaService.getFazendaAtiva();
    }

    /* =========================================================
       LISTAR ATIVAS (POR PROPRIETÁRIO)
       ========================================================= */

    public PageResponseDTO<MaquinaResponseDTO> listar(Pageable pageable) {

        Proprietario proprietario = getProprietario();

        return PageUtils.toPageResponse(
                maquinaRepository.findByFazenda_Proprietario_IdAndAtivoTrue(
                        proprietario.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVAS
       ========================================================= */

    public PageResponseDTO<MaquinaResponseDTO> listarInativas(Pageable pageable) {

        Proprietario proprietario = getProprietario();

        return PageUtils.toPageResponse(
                maquinaRepository.findByFazenda_Proprietario_IdAndAtivoFalse(
                        proprietario.getId(),
                        pageable
                ),
                this::toResponseDTO
        );
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */

    public MaquinaResponseDTO buscarPorId(Long id) {

        Maquina maquina = super.buscarEntity(id);

        return toResponseDTO(maquina);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public MaquinaResponseDTO criar(MaquinaCreateDTO dto) {

        Fazenda fazendaAtiva = getFazendaAtiva();

        RefTipoMaquina tipoMaquina = refTipoMaquinaRepository
                .findById(dto.getTipoMaquinaId())
                .orElseThrow(() -> new AuthException("Tipo de máquina não encontrado"));

        RefTipoPosseMaquina tipoPosse = refTipoPosseMaquinaRepository
                .findById(dto.getTipoPosseId())
                .orElseThrow(() -> new AuthException("Tipo de posse não encontrado"));

        Maquina maquina = new Maquina();

        maquina.setNome(dto.getNome());
        maquina.setMarca(dto.getMarca());
        maquina.setModelo(dto.getModelo());
        maquina.setDescricao(dto.getDescricao());
        maquina.setAnoFabricacao(dto.getAnoFabricacao());
        maquina.setHorimetro(dto.getHorimetro());
        maquina.setImagem(dto.getImagem());
        maquina.setTipoMaquina(tipoMaquina);
        maquina.setTipoPosse(tipoPosse);
        maquina.setValorDiaria(dto.getValorDiaria());
        maquina.setInicioLocacao(dto.getInicioLocacao());
        maquina.setFimLocacao(dto.getFimLocacao());
        maquina.setDiasContratados(dto.getDiasContratados());
        maquina.setValorTotalLocacao(dto.getValorTotalLocacao());
        maquina.setFazenda(fazendaAtiva);
        maquina.setAtivo(true);

        maquina = super.salvarEntity(maquina);

        return toResponseDTO(maquina);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public MaquinaResponseDTO atualizar(Long id, MaquinaUpdateDTO dto) {

        Maquina maquina = super.buscarEntity(id);

        RefTipoMaquina tipoMaquina = refTipoMaquinaRepository
                .findById(dto.getTipoMaquinaId())
                .orElseThrow(() -> new AuthException("Tipo de máquina não encontrado"));

        RefTipoPosseMaquina tipoPosse = refTipoPosseMaquinaRepository
                .findById(dto.getTipoPosseId())
                .orElseThrow(() -> new AuthException("Tipo de posse não encontrado"));

        maquina.setNome(dto.getNome());
        maquina.setMarca(dto.getMarca());
        maquina.setModelo(dto.getModelo());
        maquina.setDescricao(dto.getDescricao());
        maquina.setAnoFabricacao(dto.getAnoFabricacao());
        maquina.setHorimetro(dto.getHorimetro());
        maquina.setImagem(dto.getImagem());
        maquina.setTipoMaquina(tipoMaquina);
        maquina.setTipoPosse(tipoPosse);
        maquina.setValorDiaria(dto.getValorDiaria());
        maquina.setInicioLocacao(dto.getInicioLocacao());
        maquina.setFimLocacao(dto.getFimLocacao());
        maquina.setDiasContratados(dto.getDiasContratados());
        maquina.setValorTotalLocacao(dto.getValorTotalLocacao());

        maquina = super.salvarEntity(maquina);

        return toResponseDTO(maquina);
    }

    /* =========================================================
       EXCLUIR (SOFT DELETE)
       ========================================================= */

    public void excluir(Long id) {

        Maquina maquina = super.buscarEntity(id);

        maquina.setAtivo(false);

        super.salvarEntity(maquina);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    public void reativar(Long id) {

        Maquina maquina = super.buscarEntity(id);

        maquina.setAtivo(true);

        super.salvarEntity(maquina);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private MaquinaResponseDTO toResponseDTO(Maquina m) {

        return MaquinaResponseDTO.builder()
                .id(m.getId())
                .nome(m.getNome())
                .marca(m.getMarca())
                .modelo(m.getModelo())
                .descricao(m.getDescricao())
                .anoFabricacao(m.getAnoFabricacao())
                .imagem(m.getImagem())
                .horimetro(m.getHorimetro())
                .ativo(m.getAtivo())
                .fazendaId(m.getFazenda().getId())
                .tipoMaquinaId(m.getTipoMaquina().getId())
                .tipoMaquinaDescricao(m.getTipoMaquina().getDescricao())
                .tipoPosseId(m.getTipoPosse().getId())
                .tipoPosseDescricao(m.getTipoPosse().getDescricao())
                .valorDiaria(m.getValorDiaria())
                .inicioLocacao(m.getInicioLocacao())
                .fimLocacao(m.getFimLocacao())
                .diasContratados(m.getDiasContratados())
                .valorTotalLocacao(m.getValorTotalLocacao())
                .build();
    }
}