package com.wagnerquadros.simulapronaf.credito.mapper;

import com.wagnerquadros.simulapronaf.credito.dto.ItemLinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResumoResponseDto;
import com.wagnerquadros.simulapronaf.credito.entity.LinhaCredito;
import com.wagnerquadros.simulapronaf.infraestrutura.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LinhaCreditoMapper {

    private final ItemLinhaCreditoMapper itemLinhaCreditoMapper;

    public LinhaCreditoMapper(ItemLinhaCreditoMapper itemLinhaCreditoMapper) {
        this.itemLinhaCreditoMapper = itemLinhaCreditoMapper;
    }

    public LinhaCreditoResumoResponseDto paraResumoDto(LinhaCredito linhaCredito) {
        return new LinhaCreditoResumoResponseDto(
                linhaCredito.getId(),
                linhaCredito.getNome(),
                linhaCredito.getTipo(),
                linhaCredito.getDescricao(),
                linhaCredito.getAtivo()
        );
    }

    public LinhaCreditoResponseDto paraDto(LinhaCredito linhaCredito, List<ItemLinhaCreditoResponseDto> itens) {
        return new LinhaCreditoResponseDto(
                linhaCredito.getId(),
                linhaCredito.getNome(),
                linhaCredito.getTipo(),
                linhaCredito.getDescricao(),
                linhaCredito.getAtivo(),
                itens
        );
    }
}