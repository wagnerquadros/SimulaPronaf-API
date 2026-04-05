package com.wagnerquadros.simulapronaf.credito.service;

import com.wagnerquadros.simulapronaf.credito.dto.ItemLinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.entity.ItemLinhaCredito;
import com.wagnerquadros.simulapronaf.credito.mapper.ItemLinhaCreditoMapper;
import com.wagnerquadros.simulapronaf.credito.repository.ItemLinhaCreditoRepository;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemLinhaCreditoService {

    private final ItemLinhaCreditoRepository itemLinhaCreditoRepository;
    private final ItemLinhaCreditoMapper itemLinhaCreditoMapper;

    public ItemLinhaCreditoService(ItemLinhaCreditoRepository itemLinhaCreditoRepository, ItemLinhaCreditoMapper itemLinhaCreditoMapper) {
        this.itemLinhaCreditoRepository = itemLinhaCreditoRepository;
        this.itemLinhaCreditoMapper = itemLinhaCreditoMapper;
    }

    @Transactional(readOnly = true)
    public ItemLinhaCreditoResponseDto buscarPorId(Long id) {
        ItemLinhaCredito item = itemLinhaCreditoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Item da linha de crédito não encontrado com id: " + id
                ));

        return itemLinhaCreditoMapper.converterParaDto(item);
    }

    @Transactional(readOnly = true)
    public ItemLinhaCreditoResponseDto buscarPorCodigo(String codigo) {
        ItemLinhaCredito item = itemLinhaCreditoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Item da linha de crédito não encontrado com código: " + codigo
                ));

        return itemLinhaCreditoMapper.converterParaDto(item);
    }
}
