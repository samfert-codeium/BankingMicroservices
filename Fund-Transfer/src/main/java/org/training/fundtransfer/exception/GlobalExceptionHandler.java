package org.training.fundtransfer.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Global exception handler for the Fund Transfer Service.
 * 
 * <p>This class provides centralized exception handling across all controllers
 * in the Fund Transfer Service. It intercepts exceptions thrown during request
 * processing and converts them into appropriate HTTP responses with error details.</p>
 * 
 * @author Training Team
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /** Error code for bad request responses (400). */
    @Value("${spring.application.bad_request}")
    private String badRequest;

    /**
     * Handles the method argument validation exception.
     *
     * @param ex      The MethodArgumentNotValidException to handle.
     * @param headers The HttpHeaders to include in the response.
     * @param status  The HttpStatus to set in the response.
     * @param request The WebRequest associated with the request.
     * @return A ResponseEntity containing an ErrorResponse and HttpStatus.BAD_REQUEST.
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(badRequest, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the GlobalException and returns a ResponseEntity object with the appropriate error response.
     *
     * @param globalException the GlobalException object to be handled
     * @return a ResponseEntity object representing the error response
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
