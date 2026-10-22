package com.projeto.service_user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.projeto.service_user.dto.UsuarioRequestDTO;
import com.projeto.service_user.dto.UsuarioResponseDTO;
import com.projeto.service_user.exception.RegraDeNegocioException;
import com.projeto.service_user.model.Usuario;
import com.projeto.service_user.model.enums.PerfilUsuario;
import com.projeto.service_user.repository.UsuarioRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@Service
public class UsuarioService {
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
            throw new RegraDeNegocioException("O CPF informado já está cadastrado no sistema.");
        }

        if (repository.existsByEmail(emailTratado)) {
            throw new RegraDeNegocioException("O e-mail informado já está cadastrado no sistema.");
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
    public Page<UsuarioResponseDTO> buscaAvancadaUsuario(
            String nome,
            PerfilUsuario perfil,
            Boolean ativo,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Usuario> spec = Specification.anyOf();

        if (nome != null && !nome.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
        }

        if (perfil != null) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("perfil"), perfil));
        }

        if (ativo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("ativo"), ativo));
        }

        Page<Usuario> paginaEntidades = repository.findAll(spec, pageable);

        return paginaEntidades.map(this::toResponseDTO);
    }

}
