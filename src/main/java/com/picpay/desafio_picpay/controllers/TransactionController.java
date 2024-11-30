package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.TransactionDto;
import com.picpay.desafio_picpay.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    @Autowired
    TransactionService transactionService;


    @Operation(summary = "Cria uma nova transação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transação criada com sucesso."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping
    public String createTransaction (@RequestBody TransactionDto transactionDto){
        return transactionService.createTransaction(transactionDto);
    }


    @Operation(summary = "Lista todas as transações realizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todas as transações."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public List<TransactionDto> listAllTransactions (){
        return transactionService.listAllTransactions();
    }


    @Operation(summary = "Busca a transação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao buscar a transação."),
            @ApiResponse(responseCode = "404", description = "Transação não encontrada com o ID em questão."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping("/{id}")
    public Optional<TransactionDto> listTransactionById (@PathVariable Long id){
        return transactionService.listTransactionById(id);
    }
}
