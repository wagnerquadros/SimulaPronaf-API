package com.wagnerquadros.simulapronaf.autenticacao.dto;

public record LoginResponseDto(
        Long id,
        String nome,
        String email,
        Boolean autenticado
) {
}
