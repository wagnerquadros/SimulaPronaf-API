package com.wagnerquadros.simulapronaf.usuarios.service;

import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioGoogleRequestDto;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import com.wagnerquadros.simulapronaf.usuarios.repository.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDto> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaResponseDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));

        return converterParaResponseDto(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorGoogleSubject(String googleSubject) {
        return usuarioRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para o googleSubject informado."));
    }

    @Transactional
    public UsuarioResponseDto buscarOuCriarUsuarioGoogle(UsuarioGoogleRequestDto usuarioDto) {
        Usuario usuario = usuarioRepository.findByGoogleSubject(usuarioDto.googleSubject())
                .map(usuarioExistente -> atualizarDadosSeNecessario(usuarioExistente, usuarioDto))
                .orElseGet(() -> criarUsuario(usuarioDto));

        return converterParaResponseDto(usuario);
    }

    private Usuario criarUsuario(UsuarioGoogleRequestDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDto.nome());
        usuario.setEmail(usuarioDto.email());
        usuario.setGoogleSubject(usuarioDto.googleSubject());
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    private Usuario atualizarDadosSeNecessario(Usuario usuario, UsuarioGoogleRequestDto usuarioDto) {
        boolean alterou = false;

        if (!usuario.getNome().equals(usuarioDto.nome())) {
            usuario.setNome(usuarioDto.nome());
            alterou = true;
        }

        if (!usuario.getEmail().equals(usuarioDto.email())) {
            usuario.setEmail(usuarioDto.email());
            alterou = true;
        }

        if (alterou) {
            return usuarioRepository.save(usuario);
        }

        return usuario;
    }

    private UsuarioResponseDto converterParaResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }
}
