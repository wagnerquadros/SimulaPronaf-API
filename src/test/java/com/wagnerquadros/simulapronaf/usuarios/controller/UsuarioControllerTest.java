package com.wagnerquadros.simulapronaf.usuarios.controller;

import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveBloquearAcessoAoUsuariosMeSemToken() throws Exception {
        mockMvc.perform(get("/usuarios/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deveRetornarUsuarioAutenticadoComTokenValido() throws Exception {

        UsuarioResponseDto usuarioResponseDto = new UsuarioResponseDto(
                1L,
                "Nome Usuário",
                "emailteste@email.com",
                true,
                LocalDateTime.of(2026, 4, 4, 10, 46, 13)
        );

        when(usuarioService.buscarPorId(1L))
                .thenReturn(usuarioResponseDto);

        mockMvc.perform(get("/usuarios/me")
                        .with(jwt().jwt(jwt -> jwt.subject("1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Nome Usuário"))
                .andExpect(jsonPath("$.email").value("emailteste@email.com"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveBloquearAcessoAoUsuariosMeComTokenInvalido() throws Exception {
        mockMvc.perform(get("/usuarios/me")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    // Garante que o subject do JWT é usado, não um parâmetro externo manipulável
    @Test
    void naoDevePermitirQueUsuarioAcesseDadosDeOutroUsuario() throws Exception {
        // Usuário autenticado como ID 1 tenta acessar /usuarios/me
        // O endpoint deve retornar os dados do ID 1 (extraído do JWT),
        // nunca aceitar um ID de outro usuário via body/param
        UsuarioResponseDto usuarioDoToken = new UsuarioResponseDto(
                1L,
                "Usuário Legítimo",
                "legitimo@email.com",
                true,
                null
        );

        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioDoToken);

        mockMvc.perform(get("/usuarios/me")
                        .with(jwt().jwt(jwt -> jwt.subject("1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        // Verifica que o service foi chamado com o ID do TOKEN (1), não com outro
        verify(usuarioService).buscarPorId(1L);
    }

    // Testa que JWT com subject de outro usuário retorna dados desse usuário, não de outro
    @Test
    void deveUsarExclusivamenteOSubjectDoJwtParaIdentificarUsuario() throws Exception {

        UsuarioResponseDto usuarioToken2 = new UsuarioResponseDto(
                2L,
                "Outro Usuário",
                "outro@email.com",
                true,
                null
        );

        when(usuarioService.buscarPorId(2L)).thenReturn(usuarioToken2);

        mockMvc.perform(get("/usuarios/me")
                        .with(jwt().jwt(jwt -> jwt.subject("2"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        verify(usuarioService).buscarPorId(2L);
        verify(usuarioService, never()).buscarPorId(1L);
    }
}