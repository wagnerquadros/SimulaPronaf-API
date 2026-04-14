package com.wagnerquadros.simulapronaf.credito.controller;

import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResponseDto;
import com.wagnerquadros.simulapronaf.credito.dto.LinhaCreditoResumoResponseDto;
import com.wagnerquadros.simulapronaf.credito.enums.TipoLinhaCredito;
import com.wagnerquadros.simulapronaf.credito.service.LinhaCreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/credito/linhas")
public class LinhaCreditoController {

    private final LinhaCreditoService linhaCreditoService;

    public LinhaCreditoController(LinhaCreditoService linhaCreditoService) {
        this.linhaCreditoService = linhaCreditoService;
    }

    @GetMapping
    public ResponseEntity<List<LinhaCreditoResumoResponseDto>> listar(
            @RequestParam(required = false) TipoLinhaCredito tipo
    ) {
        if (tipo != null)
            return ResponseEntity.ok(linhaCreditoService.listarPorTipo(tipo));
        return ResponseEntity.ok(linhaCreditoService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LinhaCreditoResponseDto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(linhaCreditoService.buscarPorId(id));
    }
}
