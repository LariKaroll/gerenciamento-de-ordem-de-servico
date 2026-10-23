package com.projeto.service_user.dto;

import jakarta.validation.constraints.NotBlank;

public record AlterarPerfilDTO(
    @NotBlank 
    String nome,
    @NotBlank 
    String email,
    @NotBlank 
    String telefone
) {
    
}
