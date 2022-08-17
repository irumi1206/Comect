package PoolC.Comect.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity IllegalStateHandler(IllegalStateException e){
        return ResponseEntity.status(400).build();
    }

    @ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity IllegalAccessHandler(IllegalAccessException e){
        return ResponseEntity.status(401).build();
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity NoSuchElementHandler(NullPointerException e){
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity TimeOutHandler(SecurityException e){
        return ResponseEntity.status(408).build();
    }
}
