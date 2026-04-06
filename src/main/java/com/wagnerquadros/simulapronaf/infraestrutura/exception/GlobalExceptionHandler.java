package com.wagnerquadros.simulapronaf.infraestrutura.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.NOT_FOUND.value());
        resposta.put("erro", "Recurso não encontrado");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> tratarRegraDeNegocio(RegraDeNegocioException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("erro", "Regra de negócio");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, Object> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage())
        );

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.BAD_REQUEST.value());
        resposta.put("erro", "Erro de validação");
        resposta.put("campos", erros);

        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<Map<String, Object>> tratarNaoAutorizado(NaoAutorizadoException ex) {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", LocalDateTime.now());
        resposta.put("status", HttpStatus.UNAUTHORIZED.value());
        resposta.put("erro", "Não autorizado");
        resposta.put("mensagem", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

}
