package com.wagnerquadros.simulapronaf.infraestrutura.exception;

public class NaoAutorizadoException extends RuntimeException {
    public NaoAutorizadoException(String message) {
        super(message);
    }
}
