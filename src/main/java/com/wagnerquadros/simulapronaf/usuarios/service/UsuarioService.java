package com.wagnerquadros.simulapronaf.usuarios.service;

import com.wagnerquadros.simulapronaf.autenticacao.dto.UsuarioGoogleDto;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import com.wagnerquadros.simulapronaf.usuarios.mapper.UsuarioMapper;
import com.wagnerquadros.simulapronaf.usuarios.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDto buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado com id: " + id));

        return usuarioMapper.converterParaDto(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorGoogleSubject(String googleSubject) {
        return usuarioRepository.findByGoogleSubject(googleSubject)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para o googleSubject informado."));
    }

    @Transactional
    public UsuarioResponseDto buscarOuCriarUsuarioGoogle(UsuarioGoogleDto usuarioDto) {
        Usuario usuario = usuarioRepository.findByGoogleSubject(usuarioDto.googleSubject())
                .map(usuarioExistente -> atualizarDadosSeNecessario(usuarioExistente, usuarioDto))
                .orElseGet(() -> criarUsuario(usuarioDto));

        return usuarioMapper.converterParaDto(usuario);
    }

    private Usuario criarUsuario(UsuarioGoogleDto usuarioDto) {
        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDto.nome());
        usuario.setEmail(usuarioDto.email());
        usuario.setGoogleSubject(usuarioDto.googleSubject());
        usuario.setAtivo(true);
        usuario.setDataCriacao(LocalDateTime.now());

        return usuarioRepository.save(usuario);
    }

    private Usuario atualizarDadosSeNecessario(Usuario usuario, UsuarioGoogleDto usuarioDto) {
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
}
