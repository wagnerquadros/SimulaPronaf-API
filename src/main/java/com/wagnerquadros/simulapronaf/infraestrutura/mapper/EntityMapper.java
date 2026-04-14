package com.wagnerquadros.simulapronaf.infraestrutura.mapper;

public interface EntityMapper<ENTITY, DTO> {

    DTO converterParaDto(ENTITY entity);

}
