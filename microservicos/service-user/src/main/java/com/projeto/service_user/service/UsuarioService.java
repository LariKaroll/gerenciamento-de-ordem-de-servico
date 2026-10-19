package com.projeto.service_user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projeto.service_user.dto.UsuarioRequestDTO;
import com.projeto.service_user.dto.UsuarioResponseDTO;
import com.projeto.service_user.exception.ConflitoException;
import com.projeto.service_user.exception.RegraDeNegocioException;
import com.projeto.service_user.model.Usuario;
import com.projeto.service_user.model.enums.PerfilUsuario;
import com.projeto.service_user.repository.UsuarioRepository;

@Service
public class UsuarioService{
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private Usuario toEntity(UsuarioRequestDTO dto, String nome, String cpf, String email, String telefone) {
        Usuario user = new Usuario();

        user.setNome(nome);
        user.setCpf(cpf);
        user.setEmail(email);
        user.setTelefone(telefone);
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
                user.getAtivo());
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO requestDTO) {

        String nomeTratado = requestDTO.nome().trim();
        String cpfTratado = requestDTO.cpf().trim();
        String emailTratado = requestDTO.email().trim().toLowerCase();
        String telefoneTratado = requestDTO.telefone().trim();

        if (requestDTO.perfil() == PerfilUsuario.ADMINISTRADOR) {
            throw new RegraDeNegocioException(
                    "Este fluxo de cadastro só aceita a criação de perfis operacionais (técnicos e auditores).");
        }

        if (repository.existsByCpf(cpfTratado)) {
            throw new ConflitoException("O CPF informado já está cadastrado no sistema.");
        }

        if (repository.existsByEmail(emailTratado)) {
            throw new ConflitoException("O e-mail informado já está cadastrado no sistema.");
        }

        Usuario user = toEntity(requestDTO, nomeTratado, cpfTratado, emailTratado, telefoneTratado);

        Usuario userSalvo = repository.saveAndFlush(user);

        return toResponseDTO(userSalvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuario() {
        return repository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponseDTO> listarUsuariosPaginados(
            String nome, 
            com.projeto.service_user.model.enums.PerfilUsuario perfil, 
            Boolean ativo, 
            org.springframework.data.domain.Pageable paginacao) {

        return repository.findComFiltros(nome, perfil, ativo, paginacao)
                .map(this::toResponseDTO);
    }

}
