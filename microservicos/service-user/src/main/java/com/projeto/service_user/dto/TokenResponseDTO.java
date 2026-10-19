package com.projeto.service_user.dto;

public record TokenResponseDTO(
    String token,
    String tipo,
    long expiracaoEmMinutos
) {}
