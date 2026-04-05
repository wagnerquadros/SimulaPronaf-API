package com.wagnerquadros.simulapronaf.credito.dto;

import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;

public record LinhaCreditoResumoResponseDto(
        Long id,
        String nome,
        TipoLinhaCredito tipo,
        String descricao,
        Boolean ativo
) {
}
