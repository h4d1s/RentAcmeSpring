package com.pixellabs.reservation.api.exceptions;

import com.pixellabs.reservation.application.exceptions.BadRequestException;
import com.pixellabs.reservation.application.exceptions.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(
        AccessDeniedException ex,
        HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);
        problem.setTitle("Forbidden");
        problem.setDetail("You do not have permission to perform this action.");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setType(URI.create("about:blank"));

        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(problem);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(
        NotFoundException ex) {

        log.warn("Resource not found: {}", ex.getMessage());

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            ex.getMessage()
        );
        problem.setTitle("Not Found");
        problem.setType(URI.create("about:blank"));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleUnexpectedException(
        Exception ex) {

        log.error("Unexpected application error", ex);

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred."
        );
        problem.setTitle("Internal Server Error");
        problem.setType(URI.create("about:blank"));

        return ResponseEntity.internalServerError().body(problem);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ProblemDetail> handleBadRequest(
        BadRequestException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            ex.getMessage()
        );
        problem.setTitle("Bad Request");
        problem.setType(URI.create("about:blank"));
        problem.setProperty("errors", ex.getValidationErrors());

        return ResponseEntity.badRequest().body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex,
        Object body,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode statusCode,
        @NonNull WebRequest request) {
        log.warn("HTTP request error: {}", ex.getMessage());

        ProblemDetail problem = body instanceof ProblemDetail pd
            ? pd
            : ProblemDetail.forStatusAndDetail(
            statusCode,
            "The request could not be processed."
        );

        return super.handleExceptionInternal(
            ex,
            problem,
            headers,
            statusCode,
            request
        );
    }
}
