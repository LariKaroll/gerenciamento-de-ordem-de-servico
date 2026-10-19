package com.projeto.service_user.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.service_user.model.Usuario;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<Usuario> findByEmailOrCpf(String email, String cpf);
    @Query("""
        SELECT u FROM Usuario u 
        WHERE (:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%')))
          AND (:perfil IS NULL OR u.perfil = :perfil)
          AND (:ativo IS NULL OR u.ativo = :ativo)
    """)
    Page<Usuario> findComFiltros(
            String nome, 
            com.projeto.service_user.model.enums.PerfilUsuario perfil, 
            Boolean ativo, 
            org.springframework.data.domain.Pageable paginacao);
}
