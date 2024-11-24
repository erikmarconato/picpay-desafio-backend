package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.exceptions.DocumentFoundException;
import com.picpay.desafio_picpay.exceptions.EmailFoundException;
import com.picpay.desafio_picpay.exceptions.TypeUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(DocumentFoundException.class)
    public ResponseEntity<String> handleDocumentFoundException (DocumentFoundException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }

    @ExceptionHandler(EmailFoundException.class)
    public ResponseEntity<String> handleEmailFoundException (EmailFoundException exception){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

    @ExceptionHandler(TypeUserException.class)
    public ResponseEntity<String> handleTypeUserException (TypeUserException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException (HttpMessageNotReadableException exception){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Tipo de usuário inválido. Os valores permitidos são: 'common' ou 'merchant', por favor digite todos os campos corretamente.");
    }
}
