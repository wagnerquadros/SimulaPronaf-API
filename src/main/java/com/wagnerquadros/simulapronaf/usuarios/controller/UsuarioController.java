package com.wagnerquadros.simulapronaf.usuarios.controller;


import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioGoogleRequestDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioLogado(@AuthenticationPrincipal Jwt jwt) {
        Long usuarioId = Long.valueOf(jwt.getSubject());
        return ResponseEntity.ok(usuarioService.buscarPorId(usuarioId));
    }

    @PostMapping("/google")
    public ResponseEntity<UsuarioResponseDto> buscarOuCriarUsuarioGoogle(@Valid @RequestBody UsuarioGoogleRequestDto usuarioDto) {
        return ResponseEntity.ok(usuarioService.buscarOuCriarUsuarioGoogle(usuarioDto));
    }
}
