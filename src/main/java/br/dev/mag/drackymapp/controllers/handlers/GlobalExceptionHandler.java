package br.dev.mag.drackymapp.controllers.handlers;

import br.dev.mag.drackymapp.controllers.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse handleResponseStatusException(
            ResponseStatusException exception,
            HttpServletRequest request
    ) {
        var status = HttpStatus.valueOf(exception.getStatusCode().value());

        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getReason(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse handleBadCredentialsException(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        var status = HttpStatus.UNAUTHORIZED;

        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        var status = HttpStatus.BAD_REQUEST;

        var message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .orElse("Dados inválidos.");

        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleGenericException(
            Exception exception,
            HttpServletRequest request
    ) {
        var status = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "Erro interno no servidor.",
                request.getRequestURI()
        );
    }
}
