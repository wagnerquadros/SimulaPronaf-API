package com.wagnerquadros.simulapronaf.autenticacao.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.wagnerquadros.simulapronaf.autenticacao.dto.UsuarioGoogleDto;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.NaoAutorizadoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class ValidadorTokenGoogleService {

    private static final List<String> ISSUERS_VALIDOS = List.of(
            "accounts.google.com",
            "https://accounts.google.com"
    );

    @Value("${google.oauth.web-client-id}")
    private String webClientId;

    public UsuarioGoogleDto validarEExtrairUsuario(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(webClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new NaoAutorizadoException("Token do Google inválido.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            validarPayload(payload);


            String nome = (String) payload.get("name");
            String email = payload.getEmail();
            String googleSubject = payload.getSubject();

            return new UsuarioGoogleDto(
                    nome,
                    email,
                    googleSubject
            );

        } catch (GeneralSecurityException | IOException | IllegalArgumentException e) {
            throw new NaoAutorizadoException("Não foi possível validar o token do Google.");
        }
    }

    private void validarPayload(GoogleIdToken.Payload payload) {
        if (payload == null) {
            throw new NaoAutorizadoException("Payload do token do Google não encontrado.");
        }

        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new NaoAutorizadoException("O e-mail da conta Google não foi verificado.");
        }

        String issuer = payload.getIssuer();
        if (issuer == null || !ISSUERS_VALIDOS.contains(issuer)) {
            throw new NaoAutorizadoException("Issuer do token do Google é inválido.");
        }

        Long expirationTimeSeconds = payload.getExpirationTimeSeconds();
        long agoraEmSegundos = System.currentTimeMillis() / 1000;

        if (expirationTimeSeconds == null || expirationTimeSeconds <= agoraEmSegundos) {
            throw new NaoAutorizadoException("Token do Google expirado.");
        }

        String nome = (String) payload.get("name");
        if (nome == null || nome.isBlank()) {
            throw new NaoAutorizadoException("Nome do usuário não encontrado no token do Google.");
        }

        String email = payload.getEmail();
        if (email == null || email.isBlank()) {
            throw new NaoAutorizadoException("E-mail do usuário não encontrado no token do Google.");
        }

        String subject = payload.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new NaoAutorizadoException("Identificador do usuário Google não encontrado no token.");
        }
    }
}
