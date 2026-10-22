package com.projeto.service_user.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.service_user.dto.AlterarPerfilDTO;
import com.projeto.service_user.dto.AlterarSenhaDTO;
import com.projeto.service_user.dto.UsuarioRequestDTO;
import com.projeto.service_user.dto.UsuarioResponseDTO;
import com.projeto.service_user.model.enums.PerfilUsuario;
import com.projeto.service_user.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/cadastrar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO response = service.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<UsuarioResponseDTO> listar() {
        return service.listarUsuario();
    }

    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'AUDITOR')")
    public ResponseEntity<Page<UsuarioResponseDTO>> buscar(@RequestParam(required = false) String nome,
            @RequestParam(required = false) PerfilUsuario perfil,
            @RequestParam(required = false) Boolean ativo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        Page<UsuarioResponseDTO> resultado = service.buscaAvancadaUsuario(nome, perfil, ativo, page, size, sortBy,
                direction);
        return ResponseEntity.ok(resultado);
    }

    @PutMapping("/alterar_senha/{id}")
    public ResponseEntity<UsuarioResponseDTO> alterarSenha(@PathVariable UUID id, @RequestBody AlterarSenhaDTO senhaDTO) {
        
        UsuarioResponseDTO responseDTO = service.alterarSenha(id, senhaDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/alterar_perfil/{id}")
    public ResponseEntity<UsuarioResponseDTO> putMethodName(@PathVariable UUID id, @RequestBody AlterarPerfilDTO perfilDTO) {
        
        UsuarioResponseDTO responseDTO = service.alterarPerfil(id, perfilDTO);

        return ResponseEntity.ok(responseDTO);
    }

}
