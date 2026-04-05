package com.wagnerquadros.simulapronaf.autenticacao.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.NaoAutorizadoException;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioGoogleRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class ValidadorTokenGoogleService {

    @Value("${google.oauth.web-client-id}")
    private String webClientId;

    public UsuarioGoogleRequestDto validarEExtrairUsuario(String idTokenString) {
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

            String nome = (String) payload.get("name");
            String email = payload.getEmail();
            String googleSubject = payload.getSubject();

            return new UsuarioGoogleRequestDto(
                    nome,
                    email,
                    googleSubject
            );

        } catch (GeneralSecurityException | IOException e) {
            throw new NaoAutorizadoException("Não foi possível validar o token do Google.");
        }
    }
}
