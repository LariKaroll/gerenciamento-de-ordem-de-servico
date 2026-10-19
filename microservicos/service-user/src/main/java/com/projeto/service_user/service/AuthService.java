package com.projeto.service_user.service;

import com.projeto.service_user.config.JwtUtil;
import com.projeto.service_user.config.UsuarioPrincipal;
import com.projeto.service_user.dto.LoginRequestDTO;
import com.projeto.service_user.dto.TokenResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
        // Normaliza a entrada limpando caracteres especiais do CPF caso tenham sido informados com máscara
        String loginNormalizado = dados.login().trim().replaceAll("[.\\-]", "").toLowerCase();

        UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(loginNormalizado, dados.senha());
        
        // Executa a validação de credenciais e o estado ativo/inativo
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        
        // Captura o UsuarioPrincipal customizado (Sem efetuar nova consulta ao banco)
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        
        String perfil = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_TECNICO")
                .replace("ROLE_", "");

        String tokenJWT = jwtUtil.generateToken(principal.getId(), perfil);

        return new TokenResponseDTO(tokenJWT, "Bearer", jwtUtil.getExpirationMinutes());
    }
}
