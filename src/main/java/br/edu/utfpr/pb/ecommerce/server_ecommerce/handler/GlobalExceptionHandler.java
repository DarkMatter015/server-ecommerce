package br.edu.utfpr.pb.ecommerce.server_ecommerce.handler;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base.BaseException;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.handler.dto.ApiErrorDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.TranslationService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final TranslationService translator;

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorDTO> handleBaseException(BaseException exception,
                                                           HttpServletRequest request) {
        log.error("Base exception: {}", exception.getMessage());

        String message = translator.getMessageContext(exception.getCode().getMessageKey(), exception.getArgs());
        int status = exception.getCode().getStatus();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleGenericException(Exception ex,
                                                              HttpServletRequest request) {
        log.error(Arrays.toString(ex.getStackTrace()));
        log.error("Unexpected error", ex);

        String message = translator.getMessageContext("error.internal.server");
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status,
                request.getServletPath()
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleValidationArguments(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            String message = translator.getMessageContext(fieldError.getDefaultMessage(), fieldError.getArguments());
            errors.put(fieldError.getField(), message);
        }

        String message = translator.getMessageContext("fields.not.valid");
        int status = HttpStatus.BAD_REQUEST.value();

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status,
                request.getServletPath(),
                errors);

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiErrorDTO> handleFeignException(FeignException ex,
                                                            HttpServletRequest request) {
        log.error("External integration error: {}", ex.getMessage());
        int status = HttpStatus.BAD_GATEWAY.value();

        String message = translator.getMessageContext("external.integration.error");

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
        log.error("Database version conflict: {}", ex.getMessage());

        String message = translator.getMessageContext("version.conflict.error");
        HttpStatus status = HttpStatus.CONFLICT;
        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status.value(),
                request.getServletPath()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleNoResourceFoundException(NoResourceFoundException ex,
                                                                      HttpServletRequest request) {
        log.error("Resource not found: {}", ex.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND;
        String message = translator.getMessageContext("resource.not.found");

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status.value(),
                request.getServletPath()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                    HttpServletRequest request) {
        log.warn("Malformed JSON request: {}", ex.getMessage());

        String message = translator.getMessageContext("request.body.error");
        int status = HttpStatus.BAD_REQUEST.value();

        ApiErrorDTO error = new ApiErrorDTO(
                message,
                status,
                request.getServletPath()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                          HttpServletRequest request) {
        log.warn("Invalid parameter type: {}", ex.getMessage());

        String message = translator.getMessageContext("param.type.mismatch", ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : null);
        int status = HttpStatus.BAD_REQUEST.value();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                    HttpServletRequest request) {
        log.error("Database integrity violation: {}", ex.getMostSpecificCause().getMessage());

        String message = translator.getMessageContext("db.integrity.error");
        int status = HttpStatus.CONFLICT.value();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorDTO> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                HttpServletRequest request) {
        log.warn("Invalid HTTP method: {}", ex.getMethod());

        String message = translator.getMessageContext("method.not.supported", ex.getMethod());
        int status = HttpStatus.METHOD_NOT_ALLOWED.value();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDTO> handleAccessDenied(AccessDeniedException ex,
                                                          HttpServletRequest request) {
        log.warn("Access denied: {}", ex.getMessage());

        String message = translator.getMessageContext("access.denied");
        int status = HttpStatus.FORBIDDEN.value();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException ex,
                                                                               HttpServletRequest request) {
        log.warn("Authentication credentials not found: {}", ex.getMessage());

        String message = translator.getMessageContext("token.invalid");
        int status = HttpStatus.UNAUTHORIZED.value();

        ApiErrorDTO error = new ApiErrorDTO(message, status, request.getServletPath());

        return ResponseEntity.status(status).body(error);
    }
}
