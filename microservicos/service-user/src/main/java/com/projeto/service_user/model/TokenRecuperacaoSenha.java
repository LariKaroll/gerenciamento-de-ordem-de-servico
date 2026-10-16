package com.projeto.service_user.model;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tokens_recuperacao_senha")
@Getter
@Setter
public class TokenRecuperacaoSenha {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "token_hash", nullable = false)
    private String tokenHash;

    @Column(name = "expira_em", nullable = false)
    private Instant expiraEm;

    @Column(name = "usado_em")
    private Instant usadoEm;

    public TokenRecuperacaoSenha() {
    }

    public TokenRecuperacaoSenha(UUID id, Usuario usuario, String tokenHash, Instant expiraEm, Instant usadoEm) {
        this.id = id;
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.expiraEm = expiraEm;
        this.usadoEm = usadoEm;
    }

}
