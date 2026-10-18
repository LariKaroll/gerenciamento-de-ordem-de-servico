package com.projeto.service_user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.service_user.config.PasswordEncoderConfig;
import com.projeto.service_user.dto.UsuarioRequestDTO;
import com.projeto.service_user.dto.UsuarioResponseDTO;
import com.projeto.service_user.model.Usuario;
import com.projeto.service_user.model.enums.PerfilUsuario;
import com.projeto.service_user.repository.UsuarioRepository;



@Service 
public class UsuarioService {
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;


    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private Usuario toEntity(UsuarioRequestDTO dto) {
        Usuario user = new Usuario();
    
        user.setNome(dto.nome());
        user.setCpf(dto.cpf());
        user.setEmail(dto.email());
        user.setTelefone(dto.telefone());
        user.setPerfil(dto.perfil());
        user.setSenhaHash(passwordEncoder.encode(dto.senha()));

        return user;
    }

    private UsuarioResponseDTO toResponseDTO(Usuario user) {
        return new UsuarioResponseDTO(
            user.getId(), 
            user.getNome(), 
            user.getEmail(), 
            user.getTelefone(), 
            user.getPerfil(), 
            user.getOnline(), 
            user.getAtivo()
        );
    }

    @Transactional 
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {
        if(requestDTO.perfil() == PerfilUsuario.ADMINISTRADOR) {
            
        } 

    }
    
}
