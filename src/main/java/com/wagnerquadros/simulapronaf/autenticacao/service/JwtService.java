package com.wagnerquadros.simulapronaf.autenticacao.service;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    private static final long EXPIRACAO_EM_SEGUNDOS = 60 * 60;
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String gerarToken(Long usuarioId, String email) {
        Instant agora = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("simulapronaf-api")
                .issuedAt(agora)
                .expiresAt(agora.plusSeconds(EXPIRACAO_EM_SEGUNDOS))
                .subject(String.valueOf(usuarioId))
                .id(UUID.randomUUID().toString())
                .claim("roles", List.of("USER"))
                .claim("email", email)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(() -> "RS256").build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }

    public long getExpiracaoEmSegundos() {
        return EXPIRACAO_EM_SEGUNDOS;
    }
}