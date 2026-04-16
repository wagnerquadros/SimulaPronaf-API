package com.wagnerquadros.simulapronaf.autenticacao.service;

import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginGoogleRequestDto;
import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginResponseDto;
import com.wagnerquadros.simulapronaf.autenticacao.dto.DadosUsuarioGoogleDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService {

    private final UsuarioService usuarioService;
    private final ValidadorTokenGoogleService validadorTokenGoogleService;
    private final JwtService jwtService;

    public AutenticacaoService(
            UsuarioService usuarioService,
            ValidadorTokenGoogleService validadorTokenGoogleService,
            JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.validadorTokenGoogleService = validadorTokenGoogleService;
        this.jwtService = jwtService;
    }

    public LoginResponseDto autenticarComGoogle(LoginGoogleRequestDto loginDto) {

        DadosUsuarioGoogleDto dadosUsuarioGoogleDto =
                validadorTokenGoogleService.validarEExtrairUsuario(loginDto.idToken());

        UsuarioResponseDto usuarioResponseDto =
                usuarioService.buscarOuCriarUsuarioGoogle(dadosUsuarioGoogleDto);

        String accessToken = jwtService.gerarToken(
                usuarioResponseDto.id(),
                usuarioResponseDto.email()
        );

        return new LoginResponseDto(
                usuarioResponseDto.id(),
                usuarioResponseDto.nome(),
                usuarioResponseDto.email(),
                true,
                accessToken,
                "Bearer",
                jwtService.getExpiracaoEmSegundos()
        );
    }
}
