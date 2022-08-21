package PoolC.Comect.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({CustomException.class})
    public ResponseEntity<Map<String, String>> unauthorizedHandler(Exception e) {
        return ResponseEntity.status(400)
                .body(Collections.singletonMap("message", e.getMessage()));
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
