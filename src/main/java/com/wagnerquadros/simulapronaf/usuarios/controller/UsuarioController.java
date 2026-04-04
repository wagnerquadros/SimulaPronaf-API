package com.wagnerquadros.simulapronaf.usuarios.controller;

import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioGoogleRequestDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDto buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id);
    }

    @PostMapping("/google")
    public UsuarioResponseDto buscarOuCriarUsuarioGoogle(@Valid @RequestBody UsuarioGoogleRequestDto usuarioDto) {
        return usuarioService.buscarOuCriarUsuarioGoogle(usuarioDto);
    }
}
