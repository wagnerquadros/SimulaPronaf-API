package com.wagnerquadros.simulapronaf.autenticacao.controller;

import com.wagnerquadros.simulapronaf.autenticacao.dto.LoginResponseDto;
import com.wagnerquadros.simulapronaf.autenticacao.service.AutenticacaoService;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.NaoAutorizadoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoController.class)
class AutenticacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutenticacaoService autenticacaoService;

    @Test
    void deveRejeitarIdTokenVazio() throws Exception {

        String json = """
            {
              "idToken": ""
            }
        """;

        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRejeitarTokenInvalido() throws Exception {

        String json = """
            {
              "idToken": "token-invalido"
            }
        """;

        when(autenticacaoService.autenticarComGoogle(any()))
                .thenThrow(new NaoAutorizadoException("Token do Google inválido."));

        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Não autorizado"))
                .andExpect(jsonPath("$.mensagem").value("Token do Google inválido."));
    }

    @Test
    void deveAutenticarComSucessoQuandoTokenForValido() throws Exception {

        String json = """
            {
              "idToken": "token-valido"
            }
        """;

        LoginResponseDto respostaEsperada = new LoginResponseDto(
                1L,
                "Nome do Usuário",
                "emailteste@email.com",
                true,
                "jwt-token-exemplo",
                "Bearer",
                3600L
        );

        when(autenticacaoService.autenticarComGoogle(any()))
                .thenReturn(respostaEsperada);

        mockMvc.perform(post("/auth/google")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Nome do Usuário"))
                .andExpect(jsonPath("$.email").value("emailteste@email.com"))
                .andExpect(jsonPath("$.autenticado").value(true))
                .andExpect(jsonPath("$.accessToken").value("jwt-token-exemplo"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600));
    }
}