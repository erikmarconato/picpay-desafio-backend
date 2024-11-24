package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.exceptions.DocumentFoundException;
import com.picpay.desafio_picpay.exceptions.EmailFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DocumentFoundException.class)
    public ResponseEntity<String> handleCpfFoundException (DocumentFoundException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(EmailFoundException.class)
    public ResponseEntity<String> handleEmailFoundException (EmailFoundException exception){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }
}
