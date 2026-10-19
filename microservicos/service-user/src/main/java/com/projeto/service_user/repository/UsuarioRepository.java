package com.projeto.service_user.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.projeto.service_user.model.Usuario;

import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.projeto.service_user.model.enums.PerfilUsuario;


public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Optional<Usuario> findByEmailOrCpf(String email, String cpf);
    @Query(value = "SELECT * FROM usuarios WHERE " +
       "(:nome IS NULL OR nome ILIKE CONCAT('%', :nome, '%')) AND " +
       "(:perfil IS NULL OR perfil = CAST(:perfil AS character varying)) AND " +
       "(:ativo IS NULL OR ativo = :ativo) " +
       "ORDER BY nome ASC", 
       nativeQuery = true)
    Page<Usuario> findComFiltros(
            String nome, 
            PerfilUsuario perfil, 
            Boolean ativo, 
            Pageable paginacao);
}
