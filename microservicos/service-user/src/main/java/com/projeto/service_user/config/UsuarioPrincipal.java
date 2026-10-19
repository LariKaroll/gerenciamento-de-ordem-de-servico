package com.projeto.service_user.config;

import com.projeto.service_user.model.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UsuarioPrincipal implements UserDetails {

    private final UUID id;
    private final String loginUsado;
    private final String senhaHash;
    private final boolean ativo;
    private final Collection<? extends GrantedAuthority> authorities;

    public UsuarioPrincipal(Usuario usuario, String loginUsado) {
        this.id = usuario.getId();
        this.loginUsado = loginUsado;
        this.senhaHash = usuario.getSenhaHash();
        this.ativo = usuario.getAtivo();
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()));
    }

    public UUID getId() { return id; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return senhaHash; }

    @Override
    public String getUsername() { return loginUsado; }

    @Override
    public boolean isEnabled() { return ativo; } // Vincula diretamente a conta desativada

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }
}