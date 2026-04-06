package com.wagnerquadros.simulapronaf.infraestrutura.mapper;

import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.entity.LinhaCredito;

public interface EntityMapper<ENTITY, DTO> {

    DTO converterParaDto(ENTITY entity);

}
