package com.wagnerquadros.simulapronaf.infraestrutura.exception.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public record ErroRespostaDto(
        String caminho,
        String erro,
        String mensagem,
        int status,
        OffsetDateTime timestamp,
        Map<String, String> campos
) {
}