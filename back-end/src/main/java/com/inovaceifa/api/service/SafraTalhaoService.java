package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.safratalhao.*;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.mapper.PageMapper;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class SafraTalhaoService {

    private final SafraTalhaoRepository safraTalhaoRepository;
    private final TalhaoRepository talhaoRepository;
    private final RefCulturaRepository refculturaRepository;
    private final ContextoFazendaService contextoFazendaService;
    private final RefResFerrugemRepository resFerrugemRepository;
    private final RefStCultivoRepository stCultivoRepository;
    private final UsuarioRepository usuarioRepository;

    private Usuario getUsuarioLogado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthException("Usuário não autenticado");
        }

        String email = (String) authentication.getPrincipal();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));
    }

    private void validarSuperUsuario() {
        Usuario usuario = getUsuarioLogado();

        if (usuario.getPerfilId() == null || usuario.getPerfilId() != 2) {
            throw new AuthException("Apenas super usuário pode realizar esta operação");
        }
    }

    public PageResponseDTO<SafraTalhaoResponseDTO> listar(Pageable pageable) {

        Safra safraAtiva = contextoFazendaService.getSafraAtiva();

        Page<SafraTalhaoResponseDTO> page = safraTalhaoRepository
                .findBySafraIdAndAtivoTrue(safraAtiva.getId(), pageable)
                .map(this::toResponseDTO);

        return PageMapper.toPageResponse(page);
    }

    public PageResponseDTO<SafraTalhaoResponseDTO> listarInativos(Pageable pageable) {

        Safra safraAtiva = contextoFazendaService.getSafraAtiva();

        Page<SafraTalhaoResponseDTO> page = safraTalhaoRepository
                .findBySafraIdAndAtivoFalse(safraAtiva.getId(), pageable)
                .map(this::toResponseDTO);

        return PageMapper.toPageResponse(page);
    }

    public SafraTalhaoResponseDTO buscarPorId(Long id) {

        SafraTalhao st = safraTalhaoRepository.findById(id)
                .orElseThrow(() -> new AuthException("Safra talhão não encontrada"));

        validarAcesso(st);

        return toResponseDTO(st);
    }

    public SafraTalhaoResponseDTO criar(SafraTalhaoCreateDTO dto) {

        validarSuperUsuario();

        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();
        Safra safraAtiva = contextoFazendaService.getSafraAtiva();
        Proprietario proprietario = contextoFazendaService.getProprietario();

        Talhao talhao = talhaoRepository.findById(dto.getTalhaoId())
                .orElseThrow(() -> new AuthException("Talhão não encontrado"));

        if (!talhao.getFazenda().getId().equals(fazendaAtiva.getId())) {
            throw new AuthException("Talhão não pertence à fazenda ativa");
        }

        safraTalhaoRepository.findBySafraIdAndTalhaoId(
                safraAtiva.getId(), dto.getTalhaoId()
        ).ifPresent(st -> {
            throw new AuthException("Talhão já vinculado a esta safra");
        });

        RefCultura cultura = refculturaRepository.findById(dto.getCulturaId())
                .orElseThrow(() -> new AuthException("Cultura não encontrada"));

        RefResFerrugem resFerrugem = dto.getResFerrugemId() != null
                ? resFerrugemRepository.findById(dto.getResFerrugemId())
                .orElseThrow(() -> new AuthException("Resistência não encontrada"))
                : null;

        RefStCultivo stCultivo = dto.getStCultivoId() != null
                ? stCultivoRepository.findById(dto.getStCultivoId())
                .orElseThrow(() -> new AuthException("Status cultivo não encontrado"))
                : null;

        SafraTalhao st = new SafraTalhao();

        st.setProprietario(proprietario);
        st.setFazenda(fazendaAtiva);
        st.setSafra(safraAtiva);
        st.setTalhao(talhao);
        st.setCultura(cultura);

        st.setResFerrugem(resFerrugem);
        st.setStCultivo(stCultivo);

        st.setAreaUtilizada(dto.getAreaUtilizada());
        st.setEspRua(dto.getEspRua());
        st.setEspPlanta(dto.getEspPlanta());
        st.setMaterial(dto.getMaterial());
        st.setStTerra(dto.getStTerra());
        st.setVencContrato(dto.getVencContrato());
        st.setIrrigacao(dto.getIrrigacao());
        st.setEstLitroPlanta(dto.getEstLitroPlanta());
        st.setEstimativaSacaHectare(dto.getEstimativaSacaHectare());
        st.setEstimativaSaca(dto.getEstimativaSaca());

        // 🔥 NOVO
        st.setProducaoReal(dto.getProducaoReal());

        st.setAtivo(true);

        st = safraTalhaoRepository.save(st);

        return toResponseDTO(st);
    }

    public SafraTalhaoResponseDTO atualizar(Long id, SafraTalhaoUpdateDTO dto) {

        SafraTalhao st = safraTalhaoRepository.findById(id)
                .orElseThrow(() -> new AuthException("Safra talhão não encontrada"));

        validarAcesso(st);

        if (dto.getCulturaId() != null) {
            st.setCultura(refculturaRepository.findById(dto.getCulturaId())
                    .orElseThrow(() -> new AuthException("Cultura não encontrada")));
        }

        if (dto.getResFerrugemId() != null) {
            st.setResFerrugem(resFerrugemRepository.findById(dto.getResFerrugemId())
                    .orElseThrow(() -> new AuthException("Resistência não encontrada")));
        }

        if (dto.getStCultivoId() != null) {
            st.setStCultivo(stCultivoRepository.findById(dto.getStCultivoId())
                    .orElseThrow(() -> new AuthException("Sistema não encontrado")));
        }

        if (dto.getAreaUtilizada() != null) st.setAreaUtilizada(dto.getAreaUtilizada());
        if (dto.getEspRua() != null) st.setEspRua(dto.getEspRua());
        if (dto.getEspPlanta() != null) st.setEspPlanta(dto.getEspPlanta());

        st.setMaterial(dto.getMaterial());
        st.setStTerra(dto.getStTerra());
        st.setVencContrato(dto.getVencContrato());
        st.setIrrigacao(dto.getIrrigacao());
        st.setEstLitroPlanta(dto.getEstLitroPlanta());
        st.setEstimativaSacaHectare(dto.getEstimativaSacaHectare());
        st.setEstimativaSaca(dto.getEstimativaSaca());

        // 🔥 NOVO
        if (dto.getProducaoReal() != null) {
            st.setProducaoReal(dto.getProducaoReal());
        }

        st = safraTalhaoRepository.save(st);

        return toResponseDTO(st);
    }

    public void excluir(Long id) {

        validarSuperUsuario();

        SafraTalhao st = safraTalhaoRepository.findById(id)
                .orElseThrow(() -> new AuthException("Safra talhão não encontrada"));

        validarAcesso(st);

        st.setAtivo(false);

        safraTalhaoRepository.save(st);
    }

    public void reativar(Long id) {

        validarSuperUsuario();

        SafraTalhao st = safraTalhaoRepository.findById(id)
                .orElseThrow(() -> new AuthException("Safra talhão não encontrada"));

        validarAcesso(st);

        st.setAtivo(true);

        safraTalhaoRepository.save(st);
    }

    private void validarAcesso(SafraTalhao st) {

        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();

        if (!st.getFazenda().getId().equals(fazendaAtiva.getId())) {
            throw new AuthException("Acesso negado ao talhão da safra");
        }
    }

    private SafraTalhaoResponseDTO toResponseDTO(SafraTalhao st) {

        return SafraTalhaoResponseDTO.builder()
                .id(st.getId())
                .areaUtilizada(st.getAreaUtilizada())
                .estimativaSaca(st.getEstimativaSaca())

                // 🔥 NOVO
                .producaoReal(st.getProducaoReal())

                .ativo(st.getAtivo())
                .build();
    }
}