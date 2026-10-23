package com.projeto.service_user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlterarPerfilDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 120)
    String nome,
    
    @NotBlank(message = "O email é obrigatório")
    @Email (message = "Formato de email inválido")
    @Size (max = 255)
    String email,
    
    @NotBlank(message = "O número de telefone é obrigatório")
    @Pattern (
        regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$",
        message = "O telefone deve estar no formato (XX) XXXXX-XXXX"
    )
    String telefone
) {
    
}
