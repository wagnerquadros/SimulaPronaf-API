package com.wagnerquadros.simulapronaf.autenticacao.service;

import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginGoogleRequestDto;
import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioGoogleRequestDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioService usuarioService;
    private final ValidadorTokenGoogleService validadorTokenGoogleService;

    public AutenticacaoService(
            UsuarioService usuarioService,
            ValidadorTokenGoogleService validadorTokenGoogleService) {
        this.usuarioService = usuarioService;
        this.validadorTokenGoogleService = validadorTokenGoogleService;
    }

    public LoginResponseDto autenticarComGoogle(LoginGoogleRequestDto loginDto) {

        UsuarioGoogleRequestDto usuarioGoogleRequestDto =
                validadorTokenGoogleService.validarEExtrairUsuario(loginDto.idToken());

        UsuarioResponseDto usuarioResponseDto =
                usuarioService.buscarOuCriarUsuarioGoogle(usuarioGoogleRequestDto);

        return new LoginResponseDto(
                usuarioResponseDto.id(),
                usuarioResponseDto.nome(),
                usuarioResponseDto.email(),
                true
        );
    }
}
