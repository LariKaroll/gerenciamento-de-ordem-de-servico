package com.projeto.service_user.controller;

import com.projeto.service_user.dto.LoginRequestDTO;
import com.projeto.service_user.dto.TokenResponseDTO;
import com.projeto.service_user.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> logar(@RequestBody @Valid LoginRequestDTO dados) {
        TokenResponseDTO response = authService.autenticar(dados);
        return ResponseEntity.ok(response);
    }
}