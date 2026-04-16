package com.wagnerquadros.simulapronaf.credito.controller;

import com.wagnerquadros.simulapronaf.credito.service.ItemLinhaCreditoService;
import com.wagnerquadros.simulapronaf.credito.service.LinhaCreditoService;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


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
public class CreditoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LinhaCreditoService linhaCreditoService;

    @MockitoBean
    private ItemLinhaCreditoService itemLinhaCreditoService;

    // --- LinhaCreditoController ---

    @Test
    void deveBloquearListagemDeLinhasDeCreditoSemToken() throws Exception {
        mockMvc.perform(get("/credito/linhas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirListagemDeLinhasComTokenValido() throws Exception {
        when(linhaCreditoService.listarTodas()).thenReturn(List.of());

        mockMvc.perform(get("/credito/linhas")
                        .with(jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isOk());
    }

    @Test
    void deveBloquearBuscaDeLinhaPorIdSemToken() throws Exception {
        mockMvc.perform(get("/credito/linhas/1"))
                .andExpect(status().isUnauthorized());
    }

    // --- ItemLinhaCreditoController ---

    @Test
    void deveBloquearBuscaDeItemPorIdSemToken() throws Exception {
        mockMvc.perform(get("/credito/itens/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveBloquearBuscaDeItemPorCodigoSemToken() throws Exception {
        mockMvc.perform(get("/credito/itens/codigo/PRONAF-001"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void devePermitirBuscaDeItemPorCodigoComTokenValido() throws Exception {
        when(itemLinhaCreditoService.buscarPorCodigo("PRONAF-001"))
                .thenThrow(new RecursoNaoEncontradoException("não encontrado"));

        // Com token válido nunca deve retornar 401 — 404 é aceitável aqui
        mockMvc.perform(get("/credito/itens/codigo/PRONAF-001")
                        .with(jwt().jwt(j -> j.subject("1"))))
                .andExpect(status().isNotFound());
    }
}
