package com.projeto.service_user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * AlterarSenhaDTO
 */

public record AlterarSenhaDTO(
    
    @NotBlank(message = "A senha é obrigatória")
    @Size(max = 72, message = "A senha so pode ter no maximo 72 caracteres")
    @Pattern (
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\/~`]).{8,}$",
        message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um caractere especial e no mínimo 8 caracteres"
    ) 
    String novaSenha, 
    
    @NotBlank 
    String senhaAtual
) {
}