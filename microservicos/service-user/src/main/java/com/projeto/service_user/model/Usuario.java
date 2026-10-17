package com.projeto.service_user.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import com.projeto.service_user.model.enums.PerfilUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Usuario
 */

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "nome", length = 120)
    private String nome;

    @Column(name = "cpf", length = 14)
    private String cpf;

    @Column(name = "email", columnDefinition = "citext")
    private String email;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "senha_hash")
    private String senhaHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", length = 20)
    private PerfilUsuario perfil;

    @Column(name = "online", nullable = false)
    @Generated(event = EventType.INSERT)
    private Boolean online;

    @Column(name = "ativo", nullable = false)
    @Generated(event = EventType.INSERT)
    private Boolean ativo;

    @Column(name = "criado_em", updatable = false)
    @Generated(event = EventType.INSERT)
    private Instant criadoEm;

    public Usuario() {
    }

    public Usuario(UUID id, String nome, String cpf, String email, String telefone, PerfilUsuario perfil,
            Boolean online, Boolean ativo, Instant criadoEm) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.telefone = telefone;
        this.perfil = perfil;
        this.online = online;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

}