package com.picpay.desafio_picpay.exceptions;

public class TransactionEmptyException extends RuntimeException{
    public TransactionEmptyException (String message){
        super (message);
    }
}
