package com.wagnerquadros.simulapronaf.usuarios.dto;

import java.time.LocalDateTime;

public record UsuarioResponseDto(
        Long id,
        String nome,
        String email,
        Boolean ativo,
        LocalDateTime dataCriacao
) {
}
