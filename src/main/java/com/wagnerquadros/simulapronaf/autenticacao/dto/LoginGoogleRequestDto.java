package com.wagnerquadros.simulapronaf.autenticacao.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginGoogleRequestDto(
        @NotBlank(message = "idToken é obrigatório")
        String idToken
) {
}
