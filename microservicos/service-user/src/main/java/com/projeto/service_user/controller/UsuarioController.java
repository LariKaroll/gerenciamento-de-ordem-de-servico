package com.projeto.service_user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto.service_user.dto.UsuarioRequestDTO;
import com.projeto.service_user.dto.UsuarioResponseDTO;
import com.projeto.service_user.service.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/api/usuarios")
public class UsuarioController {
    @Autowired 
    private UsuarioService service;

    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO requestDTO) {
        UsuarioResponseDTO response = service.cadastrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
