package com.inovaceifa.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.model.Funcionario;
import com.inovaceifa.api.model.Proprietario;
import com.inovaceifa.api.model.Usuario;
import com.inovaceifa.api.repository.FuncionarioRepository;
import com.inovaceifa.api.repository.ProprietarioRepository;
import com.inovaceifa.api.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProprietarioRepository proprietarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // 🔓 Libera o Swagger completamente
        if (path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        // Se não tiver token → segue (Spring Security vai decidir se bloqueia)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtService.getEmailDoToken(token);
        } catch (Exception e) {
            bloquear(response, "Token JWT inválido ou expirado");
            return;
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

            if (usuario == null) {
                bloquear(response, "Usuário inválido");
                return;
            }

            // 🔑 SUPER USUÁRIO (perfil_id = 2) → ignora validações
            if (usuario.getPerfilId() != null && usuario.getPerfilId() == 2) {
                autenticar(request, email);
                filterChain.doFilter(request, response);
                return;
            }

            Proprietario proprietario;

            // 🔎 Funcionário
            Funcionario funcionario = funcionarioRepository
                    .findByUsuarioId(usuario.getId())
                    .orElse(null);

            if (funcionario != null) {
                proprietario = funcionario.getProprietario();
            } else {
                // 🔎 Proprietário
                proprietario = proprietarioRepository
                        .findByUsuario_Id(usuario.getId())
                        .orElse(null);
            }

            if (proprietario == null) {
                bloquear(response, "Proprietário não encontrado");
                return;
            }

            if (!Boolean.TRUE.equals(proprietario.getAtivo())) {
                bloquear(response, "Proprietário inativo. Acesso bloqueado.");
                return;
            }

            autenticar(request, email);
        }

        filterChain.doFilter(request, response);
    }

    /* =========================
       MÉTODOS AUXILIARES
       ========================= */

    private void autenticar(HttpServletRequest request, String email) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        null
                );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void bloquear(HttpServletResponse response, String mensagem) throws IOException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");

        ApiResponseDTO<Object> body =
                ApiResponseDTO.error(mensagem);

        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
