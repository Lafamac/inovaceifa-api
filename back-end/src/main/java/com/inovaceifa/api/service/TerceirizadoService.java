package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.terceirizado.*;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.TerceirizadoRepository;
import com.inovaceifa.api.repository.FuncionarioRepository; // 🔥 NOVO
import com.inovaceifa.api.utils.PageUtils;
import com.inovaceifa.api.validation.DocumentoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TerceirizadoService extends BaseCrudService<Terceirizado, Long> {

    private final TerceirizadoRepository repository;
    private final ContextoFazendaService contextoFazendaService;

    // 🔥 NOVO
    private final FuncionarioRepository funcionarioRepository;

    @Override
    protected JpaRepository<Terceirizado, Long> getRepository() {
        return repository;
    }

    /* =========================================================
       VALIDAÇÃO AUTOMÁTICA DE ACESSO
       ========================================================= */

    @Override
    protected void validarAcesso(Terceirizado t) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        if (!t.getFazenda().getId().equals(fazenda.getId())) {
            throw new AuthException("Terceirizado não pertence à fazenda ativa");
        }
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<TerceirizadoResponseDTO> listar(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                repository.findByFazendaIdAndAtivoTrue(fazenda.getId(), pageable),
                this::toResponse
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<TerceirizadoResponseDTO> listarInativos(Pageable pageable) {

        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        return PageUtils.toPageResponse(
                repository.findByFazendaIdAndAtivoFalse(fazenda.getId(), pageable),
                this::toResponse
        );
    }

    /* =========================================================
       BUSCAR
       ========================================================= */

    public TerceirizadoResponseDTO buscar(Long id) {
        return toResponse(super.buscarEntity(id));
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public TerceirizadoResponseDTO criar(TerceirizadoCreateDTO dto) {

        Proprietario proprietario = contextoFazendaService.getProprietario();
        Fazenda fazenda = contextoFazendaService.getFazendaAtiva();

        String cpf = DocumentoUtil.somenteNumeros(dto.getCpf());

        if (!DocumentoUtil.cpfValido(cpf))
            throw new AuthException("CPF inválido");

        // 🔥 ALTERADO (validação cruzada)
        if (repository.existsByCpf(cpf) || funcionarioRepository.existsByCpf(cpf))
            throw new AuthException("CPF já cadastrado no sistema");

        Terceirizado t = new Terceirizado();

        t.setNome(dto.getNome());
        t.setCpf(cpf);
        t.setEndereco(dto.getEndereco());
        t.setBairro(dto.getBairro());
        t.setCidade(dto.getCidade());
        t.setEstado(dto.getEstado());
        t.setEmail(dto.getEmail());
        t.setCelular(dto.getCelular());
        t.setImagem(dto.getImagem());
        t.setCargo(dto.getCargo());
        t.setSalario(dto.getSalario());
        t.setProprietario(proprietario);
        t.setFazenda(fazenda);
        t.setAtivo(true);

        t = super.salvarEntity(t);

        return toResponse(t);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public TerceirizadoResponseDTO atualizar(Long id, TerceirizadoUpdateDTO dto) {

        Terceirizado t = super.buscarEntity(id);

        if (dto.getNome() != null) t.setNome(dto.getNome());
        if (dto.getEndereco() != null) t.setEndereco(dto.getEndereco());
        if (dto.getBairro() != null) t.setBairro(dto.getBairro());
        if (dto.getCidade() != null) t.setCidade(dto.getCidade());
        if (dto.getEstado() != null) t.setEstado(dto.getEstado());
        if (dto.getEmail() != null) t.setEmail(dto.getEmail());
        if (dto.getCelular() != null) t.setCelular(dto.getCelular());
        if (dto.getImagem() != null) t.setImagem(dto.getImagem());
        if (dto.getCargo() != null) t.setCargo(dto.getCargo());
        if (dto.getSalario() != null) t.setSalario(dto.getSalario());

        t = super.salvarEntity(t);

        return toResponse(t);
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        Terceirizado t = super.buscarEntity(id);

        t.setAtivo(false);

        super.salvarEntity(t);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        Terceirizado t = super.buscarEntity(id);

        t.setAtivo(true);

        super.salvarEntity(t);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private TerceirizadoResponseDTO toResponse(Terceirizado t) {

        return TerceirizadoResponseDTO.builder()
                .id(t.getId())
                .nome(t.getNome())
                .cpf(t.getCpf())
                .email(t.getEmail())
                .celular(t.getCelular())
                .fazendaId(t.getFazenda().getId())
                .proprietarioId(t.getProprietario().getId())
                .cargo(t.getCargo())
                .salario(t.getSalario())
                .ativo(t.getAtivo())
                .build();
    }
}