package com.bcipriano.pharmacysystem.exception.resourceExceptions;

import com.bcipriano.pharmacysystem.exception.BusinessRuleException;
import com.bcipriano.pharmacysystem.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionController {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<StandartError> businessRuleException(BusinessRuleException businessRuleException, HttpServletRequest servletRequest) {
        StandartError standartError = StandartError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(businessRuleException.getMessage())
                .error("Business rule exception.")
                .path(servletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(standartError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandartError> notFoundException(NotFoundException notFoundException, HttpServletRequest servletRequest) {
        StandartError standartError = StandartError.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .message(notFoundException.getMessage())
                .error("Not found exception.")
                .path(servletRequest.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standartError);
    }

}
