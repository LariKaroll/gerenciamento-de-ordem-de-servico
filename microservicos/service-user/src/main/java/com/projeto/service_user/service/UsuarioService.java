package com.projeto.service_user.service;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.service_user.dto.AlterarPerfilDTO;
import com.projeto.service_user.dto.AlterarSenhaDTO;
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

    private static final List<String> COLUNAS_PERMITIDAS_BUSCA = List.of("id", "nome", "email", "criadoEm");
    private static final int TAMANHO_MAXIMO_PAGINA = 50;

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

        Specification<Usuario> spec = Specification.unrestricted();

        int paginaValidada = Math.max(0, page);
        int tamanhoValidado = Math.max(1, Math.min(size, TAMANHO_MAXIMO_PAGINA));

        String propriedadeOrdenacao = COLUNAS_PERMITIDAS_BUSCA.contains(sortBy) ? sortBy : "nome";

        Sort.Direction direcaoSort = Sort.Direction
                .fromOptionalString(direction != null ? direction.toUpperCase() : "ASC")
                .orElse(Sort.Direction.ASC);

        Sort sort = Sort.by(direcaoSort, propriedadeOrdenacao);
        Pageable pageable = PageRequest.of(paginaValidada, tamanhoValidado, sort);

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

    @Transactional
    public UsuarioResponseDTO alterarSenha(UUID id, AlterarSenhaDTO dto) {
        return repository.findById(id).map(u -> {

            if (!passwordEncoder.matches(dto.senhaAtual(), u.getSenhaHash())) {
                throw new RegraDeNegocioException("A senha atual digitada está incorreta");
            }

            if (dto.novaSenha().toLowerCase().contains(u.getNome().toLowerCase())) {
                throw new RegraDeNegocioException("A nova senha não pode conter o seu nome.");
            }

            String senhaCriptografada = passwordEncoder.encode(dto.novaSenha());
            u.setSenhaHash(senhaCriptografada);

            Usuario usuarioSalvo = repository.save(u);

            return toResponseDTO(usuarioSalvo);
        }).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }

    @Transactional
    public UsuarioResponseDTO alterarPerfil(UUID id, AlterarPerfilDTO dto) {
        return repository.findById(id).map(u -> {

            String nomeTratado = dto.nome().trim();
            String emailTratado = dto.email().trim().toLowerCase();
            String telefoneTratado = dto.telefone().trim();

            Boolean emailJaCadastradoPorOutroUsuario = repository.existsByEmail(emailTratado)
                    && !u.getEmail().equalsIgnoreCase(emailTratado);
            if (emailJaCadastradoPorOutroUsuario) {
                throw new RegraDeNegocioException("O e-mail informado já está em uso por outro usuário.");
            }

            u.setNome(nomeTratado);
            u.setEmail(emailTratado);
            u.setTelefone(telefoneTratado);

            Usuario usuarioSalvo = repository.save(u);
            return toResponseDTO(usuarioSalvo);
        }).orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
    }
}
