package com.projeto.service_user.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class UsuarioService{
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;
    private static final Set<String> CAMPOS_ORDENACAO_PERMITIDOS = Set.of("nome", "criadoEm", "email");
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
            PerfilUsuario perfil, 
            Boolean ativo, 
            Pageable paginacao) {

        Sort sortIdoneo = Sort.unsorted();
        for (Sort.Order order : paginacao.getSort()) {
            if (CAMPOS_ORDENACAO_PERMITIDOS.contains(order.getProperty())) {
                sortIdoneo = sortIdoneo.and(Sort.by(order.getDirection(), order.getProperty()));
            } else {
                sortIdoneo = sortIdoneo.and(Sort.by(Sort.Direction.ASC, "nome"));
            }
        }

        int tamanhoPagina = Math.min(paginacao.getPageSize(), TAMANHO_MAXIMO_PAGINA);
        Pageable paginacaoSegura = PageRequest.of(paginacao.getPageNumber(), tamanhoPagina, sortIdoneo);

        return repository.findComFiltros(nome, perfil, ativo, paginacaoSegura)
                .map(this::toResponseDTO);
    }

}
