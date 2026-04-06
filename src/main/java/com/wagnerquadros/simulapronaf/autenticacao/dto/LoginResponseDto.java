package com.wagnerquadros.simulapronaf.autenticacao.dto;

public record LoginResponseDto(
        Long id,
        String nome,
        String email,
        Boolean autenticado,
        String accessToken,
        String tokenType,
        Long expiresIn
) {
}
