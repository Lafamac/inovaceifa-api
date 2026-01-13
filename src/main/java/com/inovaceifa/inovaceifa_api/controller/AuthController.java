package com.inovaceifa.api.controller;

import com.inovaceifa.api.dto.LoginRequest;
import com.inovaceifa.api.dto.LoginResponse;
import com.inovaceifa.api.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
