package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.clients.AuthorizationClient;
import com.picpay.desafio_picpay.dtos.TransactionDto;
import com.picpay.desafio_picpay.entities.TransactionEntity;
import com.picpay.desafio_picpay.enums.UserEnum;
import com.picpay.desafio_picpay.exceptions.TransactionEmptyException;
import com.picpay.desafio_picpay.repositories.TransactionRepository;
import com.picpay.desafio_picpay.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorizationClient authorizationClient;

    @Transactional
    public String createTransaction(TransactionDto transactionDto) {
        try {
            var response = authorizationClient.getAuthorization();
            if (response.getStatusCode() == HttpStatus.OK) {
                var transactionEntity = new TransactionEntity(transactionDto);

                var verifyIdUserPayer = userRepository.findById(transactionDto.payer().getId());
                var verifyIdUserReceiver = userRepository.findById(transactionDto.receiver().getId());

                if (verifyIdUserPayer.isEmpty() || verifyIdUserReceiver.isEmpty()) {
                    return "Usuário não encontrado, verifique se o pagador e recebedor estão corretos.";
                }

                var payer = verifyIdUserPayer.get();
                var receiver = verifyIdUserReceiver.get();

                if (payer.getTypeUser() == UserEnum.merchant) {
                    return "Operação inválida: comerciantes não estão autorizados a realizar transações.";
                }

                if (Objects.equals(payer.getId(), receiver.getId())) {
                    return "Operação inválida: um usuário não pode realizar transações para si mesmo.";
                }

                if (payer.getBalance().compareTo(transactionDto.value()) < 0){
                    return "Operação inválida: o saldo do usuário é menor do que o valor informado.";
                }

                payer.setBalance(payer.getBalance().subtract(transactionDto.value()));
                userRepository.save(payer);

                receiver.setBalance(receiver.getBalance().add(transactionDto.value()));
                userRepository.save(receiver);

                transactionRepository.save(transactionEntity);

                return "Transação realizada com sucesso.";
            }
        } catch (Exception e) {
            return "Transação não autorizada pelo sistema de validação externa.";
        }

        return "Transação não autorizada, tente novamente.";
    }

    public List<TransactionDto> listAllTransactions (){
        List<TransactionEntity> transactions = transactionRepository.findAll();

        return transactions.stream().map(transactionEntity -> new TransactionDto(
                transactionEntity.getId(),
                transactionEntity.getValue(),
                transactionEntity.getPayer(),
                transactionEntity.getReceiver(),
                transactionEntity.getTransactionTime()
                )
        ).toList();
    }

    public Optional<TransactionDto> listTransactionById (Long id){

        Optional<TransactionEntity> transaction = transactionRepository.findById(id);

        if (transaction.isEmpty()){
            throw new TransactionEmptyException("Transação com o id " + id + " não encontrada.");
        }

        return transaction.map(transactionEntity -> new TransactionDto(
                transactionEntity.getId(),
                transactionEntity.getValue(),
                transactionEntity.getPayer(),
                transactionEntity.getReceiver(),
                transactionEntity.getTransactionTime()
        ));
    }
}

 