package com.wagnerquadros.simulapronaf.infraestrutura.exception;

import com.wagnerquadros.simulapronaf.infraestrutura.exception.dto.ErroRespostaDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroRespostaDto> tratarRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            HttpServletRequest request
    ) {
        ErroRespostaDto resposta = new ErroRespostaDto(
                request.getRequestURI(),
                "Recurso não encontrado",
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                OffsetDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resposta);
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ErroRespostaDto> tratarRegraDeNegocio(
            RegraDeNegocioException ex,
            HttpServletRequest request
    ) {
        ErroRespostaDto resposta = new ErroRespostaDto(
                request.getRequestURI(),
                "Regra de negócio",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                OffsetDateTime.now(),
                null
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDto> tratarValidacao(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> erros = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erro ->
                erros.put(erro.getField(), erro.getDefaultMessage())
        );

        ErroRespostaDto resposta = new ErroRespostaDto(
                request.getRequestURI(),
                "Erro de validação",
                "Um ou mais campos estão inválidos.",
                HttpStatus.BAD_REQUEST.value(),
                OffsetDateTime.now(),
                erros
        );
        return ResponseEntity.badRequest().body(resposta);
    }

    @ExceptionHandler(NaoAutorizadoException.class)
    public ResponseEntity<ErroRespostaDto> tratarNaoAutorizado(
            NaoAutorizadoException ex,
            HttpServletRequest request
    ) {
        ErroRespostaDto resposta = new ErroRespostaDto(
                request.getRequestURI(),
                "Não autorizado",
                ex.getMessage(),
                HttpStatus.UNAUTHORIZED.value(),
                OffsetDateTime.now(),
                null
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resposta);
    }

    // GlobalExceptionHandler.java — adicione este handler
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroRespostaDto> tratarMensagemIlegivel(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        ErroRespostaDto resposta = new ErroRespostaDto(
                request.getRequestURI(),
                "Requisição inválida",
                "O corpo da requisição está ausente ou malformado.",
                HttpStatus.BAD_REQUEST.value(),
                OffsetDateTime.now(),
                null
        );
        return ResponseEntity.badRequest().body(resposta);
    }
}