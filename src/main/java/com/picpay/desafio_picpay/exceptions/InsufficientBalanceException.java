package com.picpay.desafio_picpay.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException (String message){
        super (message);
    }
}
