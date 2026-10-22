package com.projeto.service_user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto.service_user.model.TokenRecuperacaoSenha;

@Repository 
public interface TokenRecuperacaoSenhaRepository extends JpaRepository<TokenRecuperacaoSenha, UUID> {
    Optional<TokenRecuperacaoSenha> findByTokenHash(String tokenHash);
}
