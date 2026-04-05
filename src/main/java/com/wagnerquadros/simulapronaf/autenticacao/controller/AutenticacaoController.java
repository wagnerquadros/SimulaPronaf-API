package com.wagnerquadros.simulapronaf.autenticacao.controller;

import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginGoogleRequestDto;
import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginResponseDto;
import com.wagnerquadros.simulapronaf.autenticacao.service.AutenticacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    public AutenticacaoController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/google")
    public ResponseEntity<LoginResponseDto> autenticarComGoogle(@RequestBody LoginGoogleRequestDto loginDto) {
        return ResponseEntity.ok(autenticacaoService.autenticarComGoogle(loginDto));
    }
}
