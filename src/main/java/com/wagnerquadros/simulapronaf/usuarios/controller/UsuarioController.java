package com.wagnerquadros.simulapronaf.usuarios.controller;

import com.wagnerquadros.simulapronaf.infraestrutura.exception.NaoAutorizadoException;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDto> buscarUsuarioLogado(@AuthenticationPrincipal Jwt jwt) {

        if (jwt == null || jwt.getSubject() == null || jwt.getSubject().isBlank()) {
            throw new NaoAutorizadoException("Token inválido: identificador do usuário ausente.");
        }

        Long usuarioId;

        try {
            usuarioId = Long.valueOf(jwt.getSubject());
        } catch (NumberFormatException e) {
            throw new NaoAutorizadoException("Token inválido: identificador do usuário malformado.");
        }

        return ResponseEntity.ok(usuarioService.buscarPorId(usuarioId));
    }
}
