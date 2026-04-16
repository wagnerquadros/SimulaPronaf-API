package com.wagnerquadros.simulapronaf.usuarios.service;

import com.wagnerquadros.simulapronaf.autenticacao.dto.DadosUsuarioGoogleDto;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import com.wagnerquadros.simulapronaf.usuarios.mapper.UsuarioMapper;
import com.wagnerquadros.simulapronaf.usuarios.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveBuscarUsuarioPorIdComSucesso() {

        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("nome do usuário");
        usuario.setEmail("emailteste@email.com");

        UsuarioResponseDto dtoEsperado =
                new UsuarioResponseDto(
                        id,
                        "nome do usuário",
                        "emailteste@email.com",
                        true,
                        null
                );

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.converterParaDto(usuario)).thenReturn(dtoEsperado);

        UsuarioResponseDto resultado = usuarioService.buscarPorId(id);

        assertEquals(dtoEsperado, resultado);

        verify(usuarioRepository).findById(id);
        verify(usuarioMapper).converterParaDto(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {

        Long id = 99L;

        when(usuarioRepository.findById(id))
                .thenReturn(Optional.empty());

        RecursoNaoEncontradoException excecao = assertThrows(
                RecursoNaoEncontradoException.class,
                () -> usuarioService.buscarPorId(id)
        );

        assertEquals("Usuário não encontrado com id: " + id, excecao.getMessage());

        verify(usuarioRepository).findById(id);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveCriarNovoUsuarioGoogleQuandoNaoExistir(){

        String googleSubject = "google-123";
        DadosUsuarioGoogleDto dadosUsuarioGoogle = new DadosUsuarioGoogleDto(
                "nome do usuário",
                "emailteste@email.com",
                googleSubject
        );

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setNome("nome do usuário");
        usuarioSalvo.setEmail("emailteste@email.com");
        usuarioSalvo.setGoogleSubject(googleSubject);
        usuarioSalvo.setAtivo(true);

        UsuarioResponseDto dtoEsperado = new UsuarioResponseDto(
                1L,
                "nome do usuário",
                "emailteste@email.com",
                true,
                usuarioSalvo.getDataCriacao()
        );

        when(usuarioRepository.findByGoogleSubject(googleSubject))
                .thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class)))
                .thenReturn(usuarioSalvo);
        when(usuarioMapper.converterParaDto(usuarioSalvo))
                .thenReturn(dtoEsperado);

        ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);

        UsuarioResponseDto resultado = usuarioService.buscarOuCriarUsuarioGoogle(dadosUsuarioGoogle);

        verify(usuarioRepository).findByGoogleSubject(googleSubject);
        verify(usuarioRepository).save(usuarioCaptor.capture());
        verify(usuarioMapper).converterParaDto(usuarioSalvo);

        Usuario usuarioCapturado = usuarioCaptor.getValue();

        assertEquals("nome do usuário", usuarioCapturado.getNome());
        assertEquals("emailteste@email.com", usuarioCapturado.getEmail());
        assertEquals("google-123", usuarioCapturado.getGoogleSubject());
        assertEquals(true, usuarioCapturado.getAtivo());

        assertEquals(dtoEsperado, resultado);
    }

    @Test
    void deveAtualizarUsuarioExistenteQuandoDadosForemDiferentes() {

        String googleSubject = "google-123";

        DadosUsuarioGoogleDto dadosUsuarioGoogle = new DadosUsuarioGoogleDto(
                "Nome do Usuário Atualizado",
                "novoemail@email.com",
                googleSubject
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome Antigo");
        usuarioExistente.setEmail("antigo@email.com");
        usuarioExistente.setGoogleSubject(googleSubject);
        usuarioExistente.setAtivo(true);

        UsuarioResponseDto dtoEsperado = new UsuarioResponseDto(
                1L,
                "Nome do Usuário Atualizado",
                "novoemail@email.com",
                true,
                usuarioExistente.getDataCriacao()
        );

        when(usuarioRepository.findByGoogleSubject(googleSubject))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioRepository.save(usuarioExistente))
                .thenReturn(usuarioExistente);

        when(usuarioMapper.converterParaDto(usuarioExistente))
                .thenReturn(dtoEsperado);

        UsuarioResponseDto resultado = usuarioService.buscarOuCriarUsuarioGoogle(dadosUsuarioGoogle);


        verify(usuarioRepository).findByGoogleSubject(googleSubject);
        verify(usuarioRepository).save(usuarioExistente);
        verify(usuarioMapper).converterParaDto(usuarioExistente);

        assertEquals("Nome do Usuário Atualizado", usuarioExistente.getNome());
        assertEquals("novoemail@email.com", usuarioExistente.getEmail());

        assertEquals(dtoEsperado, resultado);
    }

    @Test
    void naoDeveAtualizarUsuarioQuandoDadosForemIguais() {

        String googleSubject = "google-123";

        DadosUsuarioGoogleDto dadosUsuarioGoogle = new DadosUsuarioGoogleDto(
                "Nome do Usuário",
                "emailteste@email.com",
                googleSubject
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(1L);
        usuarioExistente.setNome("Nome do Usuário");
        usuarioExistente.setEmail("emailteste@email.com");
        usuarioExistente.setGoogleSubject(googleSubject);
        usuarioExistente.setAtivo(true);

        UsuarioResponseDto dtoEsperado = new UsuarioResponseDto(
                1L,
                "Nome do Usuário",
                "emailteste@email.com",
                true,
                usuarioExistente.getDataCriacao()
        );

        when(usuarioRepository.findByGoogleSubject(googleSubject))
                .thenReturn(Optional.of(usuarioExistente));

        when(usuarioMapper.converterParaDto(usuarioExistente))
                .thenReturn(dtoEsperado);

        UsuarioResponseDto resultado = usuarioService.buscarOuCriarUsuarioGoogle(dadosUsuarioGoogle);

        verify(usuarioRepository).findByGoogleSubject(googleSubject);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(usuarioMapper).converterParaDto(usuarioExistente);

        assertEquals(dtoEsperado, resultado);
    }
}
