package com.projeto.service_user.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "O campo login (E-mail ou CPF) é obrigatório.") 
        String login,

        @NotBlank(message = "A senha é obrigatória.") 
        String senha) {
}
