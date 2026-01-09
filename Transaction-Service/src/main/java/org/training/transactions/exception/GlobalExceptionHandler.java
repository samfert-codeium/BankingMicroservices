package org.training.transactions.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the Transaction Service.
 * 
 * <p>This class provides centralized exception handling across all controllers
 * in the Transaction Service. It intercepts exceptions thrown during request
 * processing and converts them into appropriate HTTP responses with error details.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles global exceptions and returns a ResponseEntity with an ErrorResponse.
     *
     * @param globalException The global exception to handle.
     * @return A ResponseEntity containing the error response.
     */

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException globalException) {

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .errorCode(globalException.getErrorCode())
                        .message(globalException.getMessage())
                        .build());
    }
}
