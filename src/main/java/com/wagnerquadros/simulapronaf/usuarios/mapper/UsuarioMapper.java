package com.wagnerquadros.simulapronaf.usuarios.mapper;

import com.wagnerquadros.simulapronaf.infraestrutura.mapper.EntityMapper;
import com.wagnerquadros.simulapronaf.usuarios.dto.UsuarioResponseDto;
import com.wagnerquadros.simulapronaf.usuarios.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper implements EntityMapper<Usuario, UsuarioResponseDto>  {

    @Override
    public UsuarioResponseDto converterParaDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAtivo(),
                usuario.getDataCriacao()
        );
    }
}
