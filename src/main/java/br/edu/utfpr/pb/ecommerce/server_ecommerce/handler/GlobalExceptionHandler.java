package br.edu.utfpr.pb.ecommerce.server_ecommerce.handler;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.ApiErrorDTO;
import feign.FeignException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleException(Exception exception,
                                    HttpServletRequest request) {

        return new ApiErrorDTO(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getServletPath());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDTO handleEntityNotFoundException(EntityNotFoundException exception,
                                       HttpServletRequest request) {

        return new ApiErrorDTO(
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getServletPath());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleExceptionArguments(MethodArgumentNotValidException exception,
                                             HttpServletRequest request) {

        BindingResult result = exception.getBindingResult();
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            errors.put( fieldError.getField(),
                    fieldError.getDefaultMessage());
        }

        return new ApiErrorDTO(
                "Fields not valid",
                HttpStatus.BAD_REQUEST.value(),
                request.getServletPath(),
                errors);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorDTO> handleFeignException(FeignException exception,
                                                         HttpServletRequest request) {

        int status = exception.status() != -1
                ? exception.status()
                : HttpStatus.BAD_REQUEST.value();

        ApiErrorDTO error = new ApiErrorDTO(
                "Error to call External API",
                status,
                request.getServletPath(),
                Map.of("detail", exception.getMessage())
        );

        return ResponseEntity.status(status).body(error);
    }
}
