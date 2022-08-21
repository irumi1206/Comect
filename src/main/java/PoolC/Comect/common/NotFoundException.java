package PoolC.Comect.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String errorMessage){
        super(errorMessage);
    }
}
