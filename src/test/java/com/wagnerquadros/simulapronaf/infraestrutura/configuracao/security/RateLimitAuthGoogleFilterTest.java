package com.wagnerquadros.simulapronaf.infraestrutura.configuracao.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
class RateLimitAuthGoogleFilterTest {

    private static final String URL_AUTH_GOOGLE = "/auth/google";
    private static final String JSON_TOKEN_INVALIDO = """
                                                        {
                                                          "idToken": "token-invalido"
                                                        }
                                                     """;
    private static final int LIMITE_REQUISICOES = 5;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void devePermitirChamadasAteOLimite() throws Exception {
        realizarChamadas(LIMITE_REQUISICOES, status().isUnauthorized());
    }

    @Test
    void deveBloquearQuandoExcederOLimite() throws Exception {
        realizarChamadas(LIMITE_REQUISICOES, status().isUnauthorized());
        realizarChamada(status().isTooManyRequests());
    }

    private void realizarChamadas(int quantidade, ResultMatcher statusEsperado) throws Exception {
        for (int i = 0; i < quantidade; i++) {
            realizarChamada(statusEsperado);
        }
    }

    private void realizarChamada(ResultMatcher statusEsperado) throws Exception {
        mockMvc.perform(post(URL_AUTH_GOOGLE)
                        .contentType("application/json")
                        .content(JSON_TOKEN_INVALIDO))
                .andExpect(statusEsperado);
    }

    // Testa bypass via X-Forwarded-For: cada IP diferente tem seu próprio contador
    @Test
    void deveAplicarRateLimitPorIpIndividual() throws Exception {
        String jsonToken = """
        { "idToken": "token-invalido" }
    """;

        // IP "1.2.3.4" faz 5 requisições — deve ser bloqueado na 6ª
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/auth/google")
                            .contentType("application/json")
                            .content(jsonToken)
                            .header("X-Forwarded-For", "1.2.3.4"))
                    .andExpect(status().isUnauthorized()); // ainda dentro do limite
        }
        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(jsonToken)
                        .header("X-Forwarded-For", "1.2.3.4"))
                .andExpect(status().isTooManyRequests()); // bloqueado

        // IP diferente "9.9.9.9" ainda deve passar normalmente
        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(jsonToken)
                        .header("X-Forwarded-For", "9.9.9.9"))
                .andExpect(status().isUnauthorized()); // não bloqueado
    }

    // Testa que o rate limit se aplica a rotas além de /auth/google
// (garantir que rotas autenticadas NÃO são afetadas pelo filtro)
    @Test
    void naoDeveAplicarRateLimitEmRotasAutenticadas() throws Exception {
        // /usuarios/me não deve ser afetado pelo RateLimitFilter
        // (o filtro só age em POST /auth/google)
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isUnauthorized()); // 401, não 429
    }

}