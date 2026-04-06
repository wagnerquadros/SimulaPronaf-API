package com.wagnerquadros.simulapronaf.credito.controller;

import com.wagnerquadros.simulapronaf.credito.dto.ItemLinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.service.ItemLinhaCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credito/itens")
public class ItemLinhaCreditoController {

    private final ItemLinhaCreditoService itemLinhaCreditoService;

    public ItemLinhaCreditoController(ItemLinhaCreditoService itemLinhaCreditoService) {
        this.itemLinhaCreditoService = itemLinhaCreditoService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemLinhaCreditoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemLinhaCreditoService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ItemLinhaCreditoResponseDto> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(itemLinhaCreditoService.buscarPorCodigo(codigo));
    }
}
