package ru.leyman.loco.locoback.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.leyman.loco.locoback.domain.dto.ErrorDto;
import ru.leyman.loco.locoback.domain.errors.ContentException;
import ru.leyman.loco.locoback.domain.errors.UnauthorizedException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ContentException.class)
    protected ResponseEntity<String> handle(ContentException e) {
        return ResponseEntity.internalServerError()
                .body(errorBody(e.getMessage()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<String> handle(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorBody(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.internalServerError()
                .body(errorBody(e.getMessage()));
    }

    private String errorBody(String message) {
        return new ErrorDto(message).toString();
    }

}
