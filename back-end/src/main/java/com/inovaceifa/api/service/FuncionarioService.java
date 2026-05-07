package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.funcionario.FuncionarioCreateDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioCriarUsuarioDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioResponseDTO;
import com.inovaceifa.api.dto.funcionario.FuncionarioUpdateDTO;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.*;
import com.inovaceifa.api.repository.FuncionarioRepository;
import com.inovaceifa.api.repository.UsuarioRepository;
import com.inovaceifa.api.repository.TerceirizadoRepository; // 🔥 NOVO
import com.inovaceifa.api.utils.PageUtils;
import com.inovaceifa.api.validation.DocumentoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class FuncionarioService extends BaseCrudService<Funcionario, Long> {

    private final FuncionarioRepository funcionarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContextoFazendaService contextoFazendaService;
    private final EmailService emailService;

    // 🔥 NOVO
    private final TerceirizadoRepository terceirizadoRepository;

    @Override
    protected JpaRepository<Funcionario, Long> getRepository() {
        return funcionarioRepository;
    }

    @Override
    protected void validarAcesso(Funcionario funcionario) {

        Proprietario proprietario = validarProprietario();

        if (!funcionario.getProprietario().getId().equals(proprietario.getId())) {
            throw new AuthException("Acesso negado");
        }
    }

    private Proprietario validarProprietario() {

        Usuario usuario = contextoFazendaService.getUsuarioLogado();

        Long perfilId = usuario.getPerfilId();

        if (perfilId == null || perfilId == 3L) {
            throw new AuthException("Você não tem permissão para gerenciar funcionários");
        }

        return contextoFazendaService.getProprietario();
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<FuncionarioResponseDTO> listar(Pageable pageable) {

        Proprietario proprietario = validarProprietario();

        return PageUtils.toPageResponse(
                funcionarioRepository.findByProprietarioId(proprietario.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<FuncionarioResponseDTO> listarInativos(Pageable pageable) {

        Proprietario proprietario = validarProprietario();

        return PageUtils.toPageResponse(
                funcionarioRepository.findByProprietarioIdAndAtivoFalse(proprietario.getId(), pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       BUSCAR POR ID (entity)
       ========================================================= */

    public Funcionario buscarPorId(Long id) {

        Proprietario proprietario = validarProprietario();

        return funcionarioRepository
                .findByIdAndProprietarioId(id, proprietario.getId())
                .orElseThrow(() -> new AuthException("Funcionário não encontrado"));
    }

    /* =========================================================
       BUSCAR POR ID (DTO)
       ========================================================= */

    public FuncionarioResponseDTO buscar(Long id) {

        Funcionario funcionario = super.buscarEntity(id);

        return toResponseDTO(funcionario);
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    public FuncionarioResponseDTO criar(FuncionarioCreateDTO dto) {

        Proprietario proprietario = validarProprietario();
        Fazenda fazendaAtiva = contextoFazendaService.getFazendaAtiva();

        String cpf = DocumentoUtil.somenteNumeros(dto.getCpf());

        if (!DocumentoUtil.cpfValido(cpf)) {
            throw new AuthException("CPF inválido");
        }

        // 🔥 VALIDAÇÃO CRUZADA
        if (funcionarioRepository.existsByCpf(cpf) || terceirizadoRepository.existsByCpf(cpf)) {
            throw new AuthException("CPF já cadastrado no sistema");
        }

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.getNome());
        funcionario.setCpf(cpf);
        funcionario.setEndereco(dto.getEndereco());
        funcionario.setBairro(dto.getBairro());
        funcionario.setCidade(dto.getCidade());
        funcionario.setEstado(dto.getEstado());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCelular(dto.getCelular());
        funcionario.setFazenda(fazendaAtiva);
        funcionario.setProprietario(proprietario);
        funcionario.setCargo(dto.getCargo());
        funcionario.setSalario(dto.getSalario());
        funcionario.setDtAdmissao(dto.getDtAdmissao());
        funcionario.setAtivo(true);

        funcionario = super.salvarEntity(funcionario);

        return toResponseDTO(funcionario);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    public FuncionarioResponseDTO atualizar(Long id, FuncionarioUpdateDTO dto) {

        Funcionario funcionario = super.buscarEntity(id);

        if (dto.getNome() != null) funcionario.setNome(dto.getNome());
        if (dto.getEndereco() != null) funcionario.setEndereco(dto.getEndereco());
        if (dto.getBairro() != null) funcionario.setBairro(dto.getBairro());
        if (dto.getCidade() != null) funcionario.setCidade(dto.getCidade());
        if (dto.getEstado() != null) funcionario.setEstado(dto.getEstado());
        if (dto.getEmail() != null) funcionario.setEmail(dto.getEmail());
        if (dto.getCelular() != null) funcionario.setCelular(dto.getCelular());
        if (dto.getCargo() != null) funcionario.setCargo(dto.getCargo());
        if (dto.getSalario() != null) funcionario.setSalario(dto.getSalario());
        if (dto.getDtAdmissao() != null) funcionario.setDtAdmissao(dto.getDtAdmissao());

        funcionario = super.salvarEntity(funcionario);

        return toResponseDTO(funcionario);
    }

    /* =========================================================
       SOFT DELETE
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        Funcionario funcionario = super.buscarEntity(id);

        funcionario.setAtivo(false);

        super.salvarEntity(funcionario);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        Funcionario funcionario = super.buscarEntity(id);

        funcionario.setAtivo(true);

        super.salvarEntity(funcionario);
    }

    /* =========================================================
       CRIAR USUÁRIO PARA FUNCIONÁRIO
       ========================================================= */

    public FuncionarioResponseDTO criarUsuarioParaFuncionario(
            Long funcionarioId,
            FuncionarioCriarUsuarioDTO dto
    ) {

        Funcionario funcionario = super.buscarEntity(funcionarioId);

        if (funcionario.getUsuario() != null) {
            throw new AuthException("Funcionário já possui usuário");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new AuthException("Email já está em uso");
        }

        String senha = gerarSenha();

        Usuario usuario = new Usuario();
        usuario.setNome(funcionario.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setPerfilId(3L);

        usuario = usuarioRepository.save(usuario);

        funcionario.setUsuario(usuario);

        super.salvarEntity(funcionario);

        emailService.enviarCredenciais(dto.getEmail(), senha);

        return toResponseDTO(funcionario);
    }

    /* =========================================================
       HELPERS
       ========================================================= */

    private String gerarSenha() {

        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            senha.append(chars.charAt(random.nextInt(chars.length())));
        }

        return senha.toString();
    }

    private FuncionarioResponseDTO toResponseDTO(Funcionario f) {

        return FuncionarioResponseDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .cpf(f.getCpf())
                .email(f.getEmail())
                .celular(f.getCelular())
                .fazendaId(f.getFazenda() != null ? f.getFazenda().getId() : null)
                .proprietarioId(f.getProprietario() != null ? f.getProprietario().getId() : null)
                .possuiUsuario(f.getUsuario() != null)
                .cargo(f.getCargo())
                .salario(f.getSalario())
                .dtAdmissao(f.getDtAdmissao())
                .ativo(f.getAtivo())
                .build();
    }
}