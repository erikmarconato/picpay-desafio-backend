package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.TransactionDto;
import com.picpay.desafio_picpay.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping
    public String createTransaction (@RequestBody TransactionDto transactionDto){
        return transactionService.createTransaction(transactionDto);
    }

    @GetMapping
    public List<TransactionDto> listAllTransactions (){
        return transactionService.listAllTransactions();
    }

    @GetMapping("/{id}")
    public Optional<TransactionDto> listTransactionById (@PathVariable Long id){
        return transactionService.listTransactionById(id);
    }
}
