package com.wagnerquadros.simulapronaf.credito.mapper;

import com.wagnerquadros.simulapronaf.credito.dto.ItemLinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.entity.ItemLinhaCredito;
import com.wagnerquadros.simulapronaf.infraestrutura.mapper.EntityMapper;
import org.springframework.stereotype.Component;

@Component
public class ItemLinhaCreditoMapper implements EntityMapper<ItemLinhaCredito, ItemLinhaCreditoResponseDto> {
    @Override
    public ItemLinhaCreditoResponseDto converterParaDto(ItemLinhaCredito item) {
        return new ItemLinhaCreditoResponseDto(
                item.getId(),
                item.getCodigo(),
                item.getTitulo(),
                item.getResumo(),
                item.getDescricao(),
                item.getPublico(),
                item.getLimite(),
                item.getJuros(),
                item.getPrazoMaximo(),
                item.getCarenciaMaxima(),
                item.getOrdemExibicao(),
                item.getIcone(),
                item.getAtivo()
        );
    }
}
