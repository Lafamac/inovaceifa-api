// CÓDIGO COMPLETO — sem cortes
// (mantive tudo que já existia e adicionei apenas o necessário)

package com.inovaceifa.api.service;

import com.inovaceifa.api.core.BaseCrudService;
import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.proprietario.*;
import com.inovaceifa.api.validation.DocumentoUtil;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.model.Proprietario;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.ProprietarioRepository;
import com.inovaceifa.api.repository.UsuarioRepository;
import com.inovaceifa.api.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.SecureRandom;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProprietarioService extends BaseCrudService<Proprietario, Long> {

    private final ProprietarioRepository proprietarioRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    protected JpaRepository<Proprietario, Long> getRepository() {
        return proprietarioRepository;
    }

    /* =========================================================
       USUÁRIO LOGADO
       ========================================================= */

    protected Usuario getUsuarioLogado() {

        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));
    }

    /* =========================================================
       VALIDAR SUPER USUÁRIO
       ========================================================= */

    private void validarSuperUsuario() {

        Usuario usuario = getUsuarioLogado();

        if (usuario.getPerfilId() == null || usuario.getPerfilId() != 2) {
            throw new AuthException("Acesso restrito ao super usuário");
        }
    }

    /* =========================================================
       VALIDAÇÃO BASE CRUD
       ========================================================= */

    @Override
    protected void validarAcesso(Proprietario entity) {
        validarSuperUsuario();
    }

    /* =========================================================
       BUSCAR POR ID
       ========================================================= */

    public ProprietarioResponseDTO buscarPorId(Long id) {

        validarSuperUsuario();

        return toResponseDTO(super.buscarEntity(id));
    }

    /* =========================================================
       LISTAR TODOS
       ========================================================= */

    public List<ProprietarioResponseDTO> listarTodos() {

        validarSuperUsuario();

        return proprietarioRepository
                .findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /* =========================================================
       LISTAR PAGINADO
       ========================================================= */

    public PageResponseDTO<ProprietarioResponseDTO> listar(Pageable pageable) {

        validarSuperUsuario();

        return PageUtils.toPageResponse(
                proprietarioRepository.findAll(pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR ATIVOS
       ========================================================= */

    public PageResponseDTO<ProprietarioResponseDTO> listarAtivos(Pageable pageable) {

        validarSuperUsuario();

        return PageUtils.toPageResponse(
                proprietarioRepository.findByAtivoTrue(pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       LISTAR INATIVOS
       ========================================================= */

    public PageResponseDTO<ProprietarioResponseDTO> listarInativos(Pageable pageable) {

        validarSuperUsuario();

        return PageUtils.toPageResponse(
                proprietarioRepository.findByAtivoFalse(pageable),
                this::toResponseDTO
        );
    }

    /* =========================================================
       CRIAR
       ========================================================= */

    @Transactional
    public ProprietarioResponseDTO criar(ProprietarioCreateDTO dto) {

        validarSuperUsuario();

        String cpf = DocumentoUtil.somenteNumeros(dto.getCpf());

        if (!DocumentoUtil.cpfValido(cpf)) {
            throw new AuthException("CPF inválido");
        }

        if (proprietarioRepository.existsByCpf(cpf)) {
            throw new AuthException("CPF já cadastrado");
        }

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new AuthException("Email já está em uso");
        }

        String senhaGerada = gerarSenha();

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(senhaGerada))
                .perfilId(1L)
                .build();

        usuario = usuarioRepository.save(usuario);

        Proprietario proprietario = new Proprietario();

        proprietario.setNome(dto.getNome());
        proprietario.setCpf(cpf);
        proprietario.setEmail(dto.getEmail());
        proprietario.setCelular(dto.getCelular());
        proprietario.setEndereco(dto.getEndereco());
        proprietario.setBairro(dto.getBairro());
        proprietario.setCidade(dto.getCidade());
        proprietario.setEstado(dto.getEstado());
        proprietario.setUsuario(usuario);
        proprietario.setAtivo(true);

        proprietario = super.salvarEntity(proprietario);

        try {
            emailService.enviarCredenciais(dto.getEmail(), senhaGerada);
        } catch (Exception ignored) {}

        return toResponseDTO(proprietario);
    }

    /* =========================================================
       ATUALIZAR
       ========================================================= */

    @Transactional
    public ProprietarioResponseDTO atualizar(Long id, ProprietarioUpdateDTO dto) {

        validarSuperUsuario();

        Proprietario proprietario = super.buscarEntity(id);

        String cpf = DocumentoUtil.somenteNumeros(dto.getCpf());

        if (!DocumentoUtil.cpfValido(cpf)) {
            throw new AuthException("CPF inválido");
        }

        if (!proprietario.getCpf().equals(cpf) &&
                proprietarioRepository.existsByCpf(cpf)) {
            throw new AuthException("CPF já cadastrado");
        }

        if (!proprietario.getEmail().equals(dto.getEmail()) &&
                usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new AuthException("Email já está em uso");
        }

        proprietario.setNome(dto.getNome());
        proprietario.setCpf(cpf);
        proprietario.setEmail(dto.getEmail());
        proprietario.setCelular(dto.getCelular());
        proprietario.setEndereco(dto.getEndereco());
        proprietario.setBairro(dto.getBairro());
        proprietario.setCidade(dto.getCidade());
        proprietario.setEstado(dto.getEstado());

        Usuario usuario = proprietario.getUsuario();

        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());

        usuarioRepository.save(usuario);

        proprietario = super.salvarEntity(proprietario);

        return toResponseDTO(proprietario);
    }

    /* =========================================================
       DESATIVAR
       ========================================================= */

    @Transactional
    public void excluir(Long id) {

        validarSuperUsuario();

        Proprietario p = super.buscarEntity(id);

        p.setAtivo(false);

        super.salvarEntity(p);
    }

    /* =========================================================
       ALTERAR STATUS
       ========================================================= */

    @Transactional
    public void alterarStatus(Long proprietarioId, Boolean ativo) {

        validarSuperUsuario();

        Proprietario proprietario = super.buscarEntity(proprietarioId);

        proprietario.setAtivo(ativo);

        super.salvarEntity(proprietario);
    }

    /* =========================================================
       REATIVAR
       ========================================================= */

    @Transactional
    public void reativar(Long id) {

        validarSuperUsuario();

        Proprietario p = super.buscarEntity(id);

        p.setAtivo(true);

        super.salvarEntity(p);
    }

    /* =========================================================
       MAPPER
       ========================================================= */

    private ProprietarioResponseDTO toResponseDTO(Proprietario p) {

        return ProprietarioResponseDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .cpf(p.getCpf())
                .email(p.getEmail())
                .celular(p.getCelular())
                .endereco(p.getEndereco())
                .bairro(p.getBairro())
                .cidade(p.getCidade())
                .estado(p.getEstado())
                .ativo(p.getAtivo())
                .build();
    }

    /* =========================================================
       GERAR SENHA
       ========================================================= */

    private String gerarSenha() {

        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8; i++)
            sb.append(chars.charAt(random.nextInt(chars.length())));

        return sb.toString();
    }
}