package com.wagnerquadros.simulapronaf.autenticacao.service;

import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=none",
        "GOOGLE_WEB_CLIENT_ID=test-client-id"
})
class JwtSegurancaTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveRejeitarTokenJwtAleatório() throws Exception {
        mockMvc.perform(get("/usuarios/me")
                        .header("Authorization", "Bearer token.invalido.qualquer"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRejeitarRequisicaoSemHeaderAuthorization() throws Exception {
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRejeitarTokenSemSubject() throws Exception {
        mockMvc.perform(get("/usuarios/me")
                        .with(jwt().jwt(jwt -> jwt.subject(null))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRejeitarTokenSemPrefixoBearer() throws Exception {
        mockMvc.perform(get("/usuarios/me")
                        .header("Authorization", "algum-token-sem-bearer"))
                .andExpect(status().isUnauthorized());
    }
}