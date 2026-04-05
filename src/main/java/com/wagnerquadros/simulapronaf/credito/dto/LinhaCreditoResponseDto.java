package com.wagnerquadros.simulapronaf.credito.dto;

import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;

import java.util.List;

public record LinhaCreditoResponseDto(
        Long id,
        String nome,
        TipoLinhaCredito tipo,
        String descricao,
        Boolean ativo,
        List<ItemLinhaCreditoResponseDto> itens
) {
}
