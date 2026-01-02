package br.edu.utfpr.pb.ecommerce.server_ecommerce.handler;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.ApiErrorDTO;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    private String getLocalizedMessage(String key, Object[] args) {
        try {
            return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return key;
        }
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorDTO> handleBaseException(BaseException exception,
                                                           HttpServletRequest request) {
        String message = getLocalizedMessage(exception.getCode().getMessageKey(), exception.getArgs());
        int status = exception.getCode().getStatus();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDTO handleGenericException(Exception ex, HttpServletRequest request) {
        log.error(Arrays.toString(ex.getStackTrace()));
        log.error(ex.getMessage());

        String message = getLocalizedMessage("error.internal.server", null);

        return new ApiErrorDTO(
                message,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getServletPath()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDTO handleValidationArguments(MethodArgumentNotValidException ex,
                                             HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            errors.put(fieldError.getField(), message);
        }

        String message = getLocalizedMessage("fields.not.valid", null);
        return new ApiErrorDTO(
                message,
                HttpStatus.BAD_REQUEST.value(),
                request.getServletPath(),
                errors);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorDTO> handleFeignException(FeignException ex,
                                                         HttpServletRequest request) {
        int status = ex.status() != -1 ? ex.status() : HttpStatus.INTERNAL_SERVER_ERROR.value();

        String message = getLocalizedMessage("external.integration.error", null);

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status,
                request.getServletPath()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorDTO> handleOptimisticLock(ObjectOptimisticLockingFailureException ex,
                                                            HttpServletRequest request) {
        log.error(ex.getMessage());

        String message = getLocalizedMessage("version.conflict.error", null);
        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status.value(),
                request.getServletPath()
        );
        return ResponseEntity.status(status).body(error);
    }
}
