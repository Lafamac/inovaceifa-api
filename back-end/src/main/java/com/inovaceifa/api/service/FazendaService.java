package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.fazenda.FazendaCreateDTO;
import com.inovaceifa.api.dto.fazenda.FazendaResponseDTO;
import com.inovaceifa.api.dto.fazenda.FazendaUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Fazenda;
import com.inovaceifa.api.model.Proprietario;
import com.inovaceifa.api.model.Safra;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.FazendaRepository;
import com.inovaceifa.api.repository.ProprietarioRepository;
import com.inovaceifa.api.repository.SafraRepository;
import com.inovaceifa.api.utils.PageUtils;
import com.inovaceifa.api.validation.DocumentoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FazendaService extends BaseCrudService<Fazenda, Long> {

    private final FazendaRepository fazendaRepository;
    private final ContextoFazendaService contextoFazendaService;
    private final ProprietarioRepository proprietarioRepository;
    private final SafraRepository safraRepository;

    @Override
    protected JpaRepository<Fazenda, Long> getRepository() {
        return fazendaRepository;
    }

    /* =========================================================
       VALIDAR ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(Fazenda fazenda) {

        Proprietario proprietario = contextoFazendaService.getProprietario();

        if (!fazenda.getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Acesso negado à fazenda");
        }
    }

    /* =========================================================
       LISTAR ATIVAS
       ========================================================= */

    public PageResponseDTO<FazendaResponseDTO> listarMinhasFazendas(Pageable pageable) {

        Proprietario proprietario = contextoFazendaService.getProprietario();

        return PageUtils.toPageResponse(
                fazendaRepository.findByProprietarioIdAndAtivoTrue(proprietario.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVAS
       ========================================================= */

    public PageResponseDTO<FazendaResponseDTO> listarInativas(Pageable pageable) {

        Proprietario proprietario = contextoFazendaService.getProprietario();

        return PageUtils.toPageResponse(
                fazendaRepository.findByProprietarioIdAndAtivoFalse(proprietario.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Transactional
    public FazendaResponseDTO criarFazenda(FazendaCreateDTO dto) {

        Usuario usuario = contextoFazendaService.getUsuarioLogado();

        if (usuario.getPerfilId() == null ||
                (usuario.getPerfilId() != 1 && usuario.getPerfilId() != 2)) {
            throw new AuthException("Apenas proprietário ou super usuário podem criar fazendas");
        }

        Proprietario proprietario;

        if (usuario.getPerfilId() == 1) {

            proprietario = contextoFazendaService.getProprietario();

        } else {

            if (dto.getProprietarioId() == null) {
                throw new AuthException("Proprietário deve ser informado");
            }

            proprietario = proprietarioRepository.findById(dto.getProprietarioId())
                    .orElseThrow(() -> new AuthException("Proprietário não encontrado"));
        }

        String cnpj = DocumentoUtil.somenteNumeros(dto.getCnpj());

        if (!DocumentoUtil.cnpjValido(cnpj)) {
            throw new AuthException("CNPJ inválido");
        }

        if (fazendaRepository.existsByCnpj(cnpj)) {
            throw new AuthException("Já existe uma fazenda cadastrada com este CNPJ");
        }

        Fazenda fazenda = new Fazenda();

        fazenda.setProprietario(proprietario);
        fazenda.setNome(dto.getNome());
        fazenda.setCnpj(cnpj);
        fazenda.setEndereco(dto.getEndereco());
        fazenda.setCidade(dto.getCidade());
        fazenda.setEstado(dto.getEstado());
        fazenda.setAtivo(true);

        fazenda = super.salvarEntity(fazenda);

        String nomeSafra = dto.getNomeSafraInicial();

        if (nomeSafra == null || nomeSafra.isBlank()) {
            nomeSafra = "Safra Inicial";
        }

        Safra safra = new Safra();

        safra.setFazenda(fazenda);
        safra.setNome(nomeSafra);
        safra.setDataInicial(LocalDate.now());
        safra.setDataFinal(LocalDate.now().plusYears(1));

        safra = safraRepository.save(safra);

        fazenda.setSafraAtiva(safra);

        fazenda = super.salvarEntity(fazenda);

        return toResponseDTO(fazenda);
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public FazendaResponseDTO buscarPorId(Long id) {

        Fazenda fazenda = super.buscarEntity(id);

        return toResponseDTO(fazenda);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public FazendaResponseDTO atualizar(Long id, FazendaUpdateDTO dto) {

        Fazenda fazenda = super.buscarEntity(id);

        fazenda.setNome(dto.getNome());
        fazenda.setEndereco(dto.getEndereco());
        fazenda.setCidade(dto.getCidade());
        fazenda.setEstado(dto.getEstado());

        if (dto.getSafraAtivaId() != null) {

            Safra safra = safraRepository.findById(dto.getSafraAtivaId())
                    .orElseThrow(() -> new AuthException("Safra não encontrada"));

            fazenda.setSafraAtiva(safra);
        }

        fazenda = super.salvarEntity(fazenda);

        return toResponseDTO(fazenda);
    }

    /* =========================================================
       EXCLUIR
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        Fazenda fazenda = super.buscarEntity(id);

        fazenda.setAtivo(false);

        super.salvarEntity(fazenda);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        Fazenda fazenda = super.buscarEntity(id);

        fazenda.setAtivo(true);

        super.salvarEntity(fazenda);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private FazendaResponseDTO toResponseDTO(Fazenda f) {

        return FazendaResponseDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .cnpj(f.getCnpj())
                .endereco(f.getEndereco())
                .cidade(f.getCidade())
                .estado(f.getEstado())
                .safraAtivaId(
                        f.getSafraAtiva() != null
                                ? f.getSafraAtiva().getId()
                                : null
                )
                .build();
    }
}