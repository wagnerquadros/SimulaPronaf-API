package com.wagnerquadros.simulapronaf.autenticacao.controller;

import com.wagnerquadros.simulapronaf.autenticacao.service.AutenticacaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoController.class)
class AutenticacaoInputValidacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    // Testa múltiplos payloads inválidos com teste parametrizado
    @ParameterizedTest
    @ValueSource(strings = {
            "",                          // vazio
            "{}",                        // JSON sem campo idToken
            "{\"idToken\": null}",       // idToken nulo
            "{\"idToken\": \"\"}",       // idToken string vazia
            "{\"outro\": \"campo\"}",    // campo errado
    })
    void deveRejeitarPayloadsInvalidos(String body) throws Exception {
        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    // Testa Content-Type errado
    @Test
    void deveRejeitarContentTypeErrado() throws Exception {
        mockMvc.perform(post("/auth/google")
                        .contentType("text/plain")
                        .content("token-qualquer"))
                .andExpect(status().isUnsupportedMediaType());
    }

    // Testa que a resposta de erro NÃO vaza stack trace
    @Test
    void respostaDeErroNaoDeveVazarStackTrace() throws Exception {
        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content("{\"idToken\": \"\"}"))
                .andExpect(status().isBadRequest())
                // Garante que não há campos de debug na resposta
                .andExpect(result -> {
                    String body = result.getResponse().getContentAsString();
                    assertFalse(body.contains("at com.wagnerquadros"));
                    assertFalse(body.contains("Exception"));
                    assertFalse(body.contains("trace"));
                });
    }
}