package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.LoginRequestDTO;
import com.inovaceifa.api.dto.LoginResponseDTO;
import com.inovaceifa.api.dto.ApiResponseDTO;
import com.inovaceifa.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponseDTO<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
        return ApiResponseDTO.success(
                authService.login(dto),
                "Login realizado com sucesso"
        );
    }
}
