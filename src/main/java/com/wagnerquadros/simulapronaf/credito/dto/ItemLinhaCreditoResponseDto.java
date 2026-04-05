package com.wagnerquadros.simulapronaf.credito.dto;

import java.math.BigDecimal;

public record ItemLinhaCreditoResponseDto(
        Long id,
        String codigo,
        String titulo,
        String resumo,
        String descricao,
        String publico,
        BigDecimal limite,
        BigDecimal juros,
        Integer prazoMaximo,
        Integer carenciaMaxima,
        Integer ordemExibicao,
        String icone,
        Boolean ativo
) {
}
