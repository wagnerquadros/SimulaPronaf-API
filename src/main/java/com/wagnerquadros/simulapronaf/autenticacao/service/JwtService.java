package com.wagnerquadros.simulapronaf.autenticacao.service;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String gerarToken(Long usuarioId) {
        Instant agora = Instant.now();
        long expiracaoEmSegundos = 60 * 60; // 1 hora

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("simulapronaf-api")
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(expiracaoEmSegundos))
                .subject(String.valueOf(usuarioId))
                .claim("scope", "USER")
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    public long getExpiracaoEmSegundos() {
        return 60 * 60;
    }
}