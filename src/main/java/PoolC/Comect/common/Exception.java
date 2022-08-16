package PoolC.Comect.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class Exception {
    //400
    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<Object> BadRequest(InterruptedException e){
        return ResponseEntity.status(400).build();
    }
    //401
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> Unauthorized(IllegalStateException e){
        return ResponseEntity.status(401).build();
    }
    //404 not found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> NotFound(NoSuchElementException e){
        return ResponseEntity.status(404).build();
    }
//    //408, request time out
//    @ExceptionHandler
//    public ResponseEntity<Object> RequestTimeOut(SecurityException e){
//        return ResponseEntity.status(408).build();
//    }



}
