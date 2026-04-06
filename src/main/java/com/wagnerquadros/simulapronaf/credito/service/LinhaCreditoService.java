package com.wagnerquadros.simulapronaf.credito.service;

import com.wagnerquadros.simulapronaf.credito.dto.ItemLinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResumoResponseDto;
import com.wagnerquadros.simulapronaf.credito.entity.ItemLinhaCredito;
import com.wagnerquadros.simulapronaf.credito.entity.LinhaCredito;
import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;
import com.wagnerquadros.simulapronaf.credito.mapper.ItemLinhaCreditoMapper;
import com.wagnerquadros.simulapronaf.credito.mapper.LinhaCreditoMapper;
import com.wagnerquadros.simulapronaf.credito.repository.ItemLinhaCreditoRepository;
import com.wagnerquadros.simulapronaf.credito.repository.LinhaCreditoRepository;
import com.wagnerquadros.simulapronaf.infraestrutura.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LinhaCreditoService {

    private final LinhaCreditoRepository linhaCreditoRepository;
    private final ItemLinhaCreditoRepository itemLinhaCreditoRepository;
    private final LinhaCreditoMapper linhaCreditoMapper;
    private final ItemLinhaCreditoMapper itemLinhaCreditoMapper;

    public LinhaCreditoService(
            LinhaCreditoRepository linhaCreditoRepository,
            ItemLinhaCreditoRepository itemLinhaCreditoRepository,
            LinhaCreditoMapper linhaCreditoMapper,
            ItemLinhaCreditoMapper itemLinhaCreditoMapper
    ) {
        this.linhaCreditoRepository = linhaCreditoRepository;
        this.itemLinhaCreditoRepository = itemLinhaCreditoRepository;
        this.linhaCreditoMapper = linhaCreditoMapper;
        this.itemLinhaCreditoMapper = itemLinhaCreditoMapper;
    }

    @Transactional(readOnly = true)
    public List<LinhaCreditoResumoResponseDto> listarTodas() {
        return linhaCreditoRepository.findByAtivoTrue()
                .stream()
                .map(linhaCreditoMapper::paraResumoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<LinhaCreditoResumoResponseDto> listarPorTipo(TipoLinhaCredito tipo) {
        return linhaCreditoRepository.findByTipoAndAtivoTrue(tipo)
                .stream()
                .map(linhaCreditoMapper::paraResumoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public LinhaCreditoResponseDto buscarPorId(Long id) {
        LinhaCredito linhaCredito = linhaCreditoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Linha de crédito não encontrada com id: " + id
                ));

        List<ItemLinhaCreditoResponseDto> itens = itemLinhaCreditoRepository
                .findByLinhaCreditoIdAndAtivoTrueOrderByOrdemExibicaoAsc(id)
                .stream()
                .map(itemLinhaCreditoMapper::converterParaDto)
                .toList();

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
