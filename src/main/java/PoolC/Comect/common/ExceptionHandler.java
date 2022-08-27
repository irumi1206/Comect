package PoolC.Comect.common;

import PoolC.Comect.relation.controller.RelationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);


    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runtimeHandler(RuntimeException e){
        return ResponseEntity.status(400).build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customHandler(CustomException e){
        logger.error("Error! Code: "+e.getErrorCode());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    //////////////////////////

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
