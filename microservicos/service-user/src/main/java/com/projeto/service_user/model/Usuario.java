package com.projeto.service_user.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.*;
import org.hibernate.generator.EventType;

import com.projeto.service_user.model.enums.PerfilUsuario;

import jakarta.persistence.*;

/**
 * Usuario
 */

@Entity
@Table(name = "usuarios")
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

    @Column(name = "criado_em")
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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Instant criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

}