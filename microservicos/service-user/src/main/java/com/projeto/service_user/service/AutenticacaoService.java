package com.projeto.service_user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.projeto.service_user.config.UsuarioPrincipal;
import com.projeto.service_user.model.Usuario;
import com.projeto.service_user.model.util.LoginNormalizer;
import com.projeto.service_user.repository.UsuarioRepository;

@Service
public class AutenticacaoService implements UserDetailsService {

    private final UsuarioRepository repository;

    public AutenticacaoService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        String loginNormalizado = LoginNormalizer.normalizar(login);

        Usuario usuario = repository.findByEmailOrCpf(loginNormalizado, loginNormalizado)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));

        return new UsuarioPrincipal(usuario, login);
    }
}
