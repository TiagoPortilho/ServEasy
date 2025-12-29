package com.tiagoportilho.ServEasy.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há erro de validação nos dados de entrada.
 */
public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }

    public ValidationException(String field, String message) {
        super("Erro de validação no campo '" + field + "': " + message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
}
