package com.picpay.desafio_picpay.exceptions;

public class UserEmptyException extends RuntimeException{
    public UserEmptyException (String message){
        super (message);
    }
}
