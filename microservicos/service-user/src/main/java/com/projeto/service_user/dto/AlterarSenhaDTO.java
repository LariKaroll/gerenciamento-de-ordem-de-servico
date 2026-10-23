package com.projeto.service_user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

/**
 * AlterarSenhaDTO
 */

public record AlterarSenhaDTO(

        @NotBlank @NotBlank(message = "A senha é obrigatória") 
        @Size(min = 8, max = 72, message = "A senha so pode ter no maximo 72 caracteres") 
        String senhaAtual,

        @NotBlank(message = "A nova senha não pode estar em branco") 
        @Size(min = 8, max = 72, message = "A nova senha deve ter entre 8 e 72 caracteres") 
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?\":{}|<>_\\-+=\\[\\]\\\\/~`]).{8,}$", 
                message = "A senha deve conter pelo menos uma letra maiúscula, uma letra minúscula, um caractere especial e no mínimo 8 caracteres") 
        String novaSenha

         ){
}