package com.leaguetracker.app.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler({
            AccountNotFoundException.class,
            SummonerNotFoundException.class,
            LeagueNotFoundException.class,
            MatchNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFoundException(
            RuntimeException e,
            HttpServletRequest request) {
        log.warn("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiError> handleInvalidRequestException(
            InvalidRequestException e,
            HttpServletRequest request) {
        log.warn("Invalid request: {}", e.getMessage());
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SummonerRecentlyUpdatedException.class)
    public ResponseEntity<ApiError> handleSummonerRecentlyUpdatedException(
            SummonerRecentlyUpdatedException e,
            HttpServletRequest request) {
        log.warn("Summoner recently updated: {}", e.getMessage());
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(
            Exception e,
            HttpServletRequest request) throws Exception {

        String path = request.getRequestURI();

        // Skip actuator endpoints and favicon
        if (path.startsWith("/actuator/") || path.equals("/actuator") || path.equals("/favicon.ico") || path.equals("/metrics")) {
            throw e;  // let Spring handle it internally
        }
        

        log.error("Unexpected error: {}", e.getMessage(), e);

        ApiError apiError = new ApiError(
                path,
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
