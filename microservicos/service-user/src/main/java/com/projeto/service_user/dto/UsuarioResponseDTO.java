package com.projeto.service_user.dto;

import java.util.UUID;

import com.projeto.service_user.model.enums.PerfilUsuario;

public record UsuarioResponseDTO(
    UUID id,
    String nome,
    String email,
    String telefone,
    PerfilUsuario perfil,
    Boolean online,
    Boolean ativo
) {}
