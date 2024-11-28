package com.picpay.desafio_picpay.dtos;

import com.picpay.desafio_picpay.entities.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto (
        Long id,
        BigDecimal value,
        UserEntity payer,
        UserEntity receiver,
        LocalDateTime transactionTime
){
}
