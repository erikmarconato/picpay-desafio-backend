package com.picpay.desafio_picpay.exceptions;

public class UnauthorizedTransactionMerchantException extends RuntimeException{
    public UnauthorizedTransactionMerchantException (String message){
        super (message);
    }
}
