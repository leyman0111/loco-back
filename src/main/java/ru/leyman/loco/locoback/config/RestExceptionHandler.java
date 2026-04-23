package ru.leyman.loco.locoback.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.leyman.loco.locoback.domain.errors.ContentException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ContentException.class)
    protected ResponseEntity<String> handle(ContentException e) {
        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handle(Exception e) {
        return ResponseEntity.internalServerError()
                .body(e.getMessage());
    }

}
