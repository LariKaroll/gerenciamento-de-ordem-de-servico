package com.projeto.service_user.service;

import com.projeto.service_user.config.JwtUtil;
import com.projeto.service_user.config.UsuarioPrincipal;
import com.projeto.service_user.dto.LoginRequestDTO;
import com.projeto.service_user.dto.TokenResponseDTO;
import com.projeto.service_user.model.util.LoginNormalizer;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public TokenResponseDTO autenticar(LoginRequestDTO dados) {
        String loginNormalizado = LoginNormalizer.normalizer(dados.login());

        UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginNormalizado, dados.senha());
        
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        
        String tokenJWT = jwtUtil.generateToken(principal.getId(), principal.getPerfil().name());

        return new TokenResponseDTO(tokenJWT, "Bearer", jwtUtil.getExpirationMinutes());
    }
}
