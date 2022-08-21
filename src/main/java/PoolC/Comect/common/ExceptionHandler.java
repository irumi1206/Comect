package PoolC.Comect.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> NotFoundHandler(NotFoundException e){
        return ResponseEntity.status(400).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity IllegalStateHandler(IllegalStateException e){
        return ResponseEntity.status(400).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalAccessException.class)
    public ResponseEntity IllegalAccessHandler(IllegalAccessException e){
        return ResponseEntity.status(401).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NullPointerException.class)
    public ResponseEntity NoSuchElementHandler(NullPointerException e){
        return ResponseEntity.status(404).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SecurityException.class)
    public ResponseEntity TimeOutHandler(SecurityException e){
        return ResponseEntity.status(408).build();
    }
}
