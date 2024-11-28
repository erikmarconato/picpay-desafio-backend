package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.TransactionDto;
import com.picpay.desafio_picpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public String createTransaction (@RequestBody TransactionDto transactionDto){
        return transactionService.createTransaction(transactionDto);
    }
}
