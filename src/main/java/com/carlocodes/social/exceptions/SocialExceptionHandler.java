package com.carlocodes.social.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.OffsetDateTime;

@RestControllerAdvice
public class SocialExceptionHandler {
    @ExceptionHandler(SocialException.class)
    public ResponseEntity<ErrorResponse> handleException(SocialException e) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setTimestamp(OffsetDateTime.now());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError(e.getMessage());
        errorResponse.setPath(getCurrentPath());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ValidationErrorResponse> handleBindException(BindException e) {
        // TODO: Maybe need to map the field errors in the existing error response
        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse();

        validationErrorResponse.setTimestamp(OffsetDateTime.now());
        validationErrorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        validationErrorResponse.setPath(getCurrentPath());

        for (FieldError fieldError : e.getFieldErrors()) {
            validationErrorResponse.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationErrorResponse);
    }

    private String getCurrentPath() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return servletRequestAttributes.getRequest().getRequestURI();
    }
}
