package com.inovaceifa.api.exception;

import com.inovaceifa.api.dto.ApiResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    /* ===============================
       401 - NÃO AUTENTICADO
       =============================== */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /* ===============================
       403 - SEM PERMISSÃO
       =============================== */
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleForbidden(AuthException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /* ===============================
       404 - NÃO ENCONTRADO
       =============================== */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /* ===============================
       409 - CONFLITO DE CONTEXTO
       =============================== */
    @ExceptionHandler({
            ContextoFazendaInexistenteException.class,
            ContextoSafraInexistenteException.class
    })
    public ResponseEntity<ApiResponseDTO<Void>> handleContext(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponseDTO.error(ex.getMessage()));
    }

    /* ===============================
       400 - VALIDAÇÃO
       =============================== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDTO<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e -> erros.put(e.getField(), e.getDefaultMessage()));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDTO.validationError("Erro de validação", erros));
    }

    /* ===============================
       500 - ERRO INTERNO
       =============================== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDTO<Void>> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponseDTO.error("Erro interno do servidor"));
    }
}
