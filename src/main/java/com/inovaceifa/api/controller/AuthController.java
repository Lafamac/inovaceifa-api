package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.BaseResponseDTO;
import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDTO<LoginResponseDTO>> login(
            @RequestBody LoginRequestDTO dto
    ) {
        LoginResponseDTO response = authService.login(dto);

        return ResponseEntity.ok(
                new BaseResponseDTO<>(
                        true,
                        "Login realizado com sucesso",
                        response
                )
        );
    }
}
