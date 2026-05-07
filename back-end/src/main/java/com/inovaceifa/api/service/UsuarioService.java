package com.inovaceifa.api.service;

import com.inovaceifa.api.dto.pagination.PageResponseDTO;
import com.inovaceifa.api.dto.usuario.UsuarioCreateDTO;
import com.inovaceifa.api.dto.usuario.UsuarioResponseDTO;
import com.inovaceifa.api.dto.usuario.UsuarioUpdateDTO;
import com.inovaceifa.api.exception.AuthException;
import com.inovaceifa.api.mapper.PageMapper;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;   // 👈 CORRETO

    public PageResponseDTO<UsuarioResponseDTO> listar(Pageable pageable) {

        Page<UsuarioResponseDTO> page = usuarioRepository
                .findAll(pageable)
                .map(this::toResponseDTO);

        return PageMapper.toPageResponse(page);
    }

    public UsuarioResponseDTO criar(UsuarioCreateDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new AuthException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .perfilId(dto.getPerfilId())
                .criadoEm(LocalDateTime.now())
                .build();

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    public UsuarioResponseDTO buscarPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        return toResponseDTO(usuario);
    }

    public UsuarioResponseDTO atualizar(Long id, UsuarioUpdateDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        if (dto.getPerfilId() != null) usuario.setPerfilId(dto.getPerfilId());

        return toResponseDTO(usuarioRepository.save(usuario));
    }

    public void deletar(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
    }

    private UsuarioResponseDTO toResponseDTO(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .nome(u.getNome())
                .email(u.getEmail())
                .perfilId(u.getPerfilId())
                .build();
    }
}
