package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.clients.AuthorizationClient;
import com.picpay.desafio_picpay.dtos.TransactionDto;
import com.picpay.desafio_picpay.entities.TransactionEntity;
import com.picpay.desafio_picpay.entities.UserEntity;
import com.picpay.desafio_picpay.enums.UserEnum;
import com.picpay.desafio_picpay.exceptions.TransactionEmptyException;
import com.picpay.desafio_picpay.repositories.TransactionRepository;
import com.picpay.desafio_picpay.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthorizationClient authorizationClient;

    @InjectMocks
    TransactionService transactionService;

    @Test
    @DisplayName("Should successfully create a transaction")
    void createTransactionSuccess(){
        UserEntity payerCommon = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.common,
                BigDecimal.valueOf(500));

        UserEntity receiverMerchant = new UserEntity(
                2L,
                "RecebedorNome",
                "109.876.543-21",
                "emailcomerciante@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(500),
                payerCommon,
                receiverMerchant,
                LocalDateTime.now()
        );

        ArgumentCaptor<UserEntity> argumentCaptorUsers = ArgumentCaptor.forClass(UserEntity.class);
        ArgumentCaptor<TransactionEntity> argumentCaptorTransaction = ArgumentCaptor.forClass(TransactionEntity.class);

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(userRepository.findById(transactionDto.payer().getId())).thenReturn(Optional.of(payerCommon));
        Mockito.when(userRepository.findById(transactionDto.receiver().getId())).thenReturn(Optional.of(receiverMerchant));

        String response = transactionService.createTransaction(transactionDto);

        Mockito.verify(userRepository, Mockito.times(2)).save(argumentCaptorUsers.capture());
        Mockito.verify(transactionRepository, Mockito.times(1)).save(argumentCaptorTransaction.capture());

        var savedBalanceUser = argumentCaptorUsers.getAllValues();
        Assertions.assertEquals(BigDecimal.valueOf(0), savedBalanceUser.get(0).getBalance());
        Assertions.assertEquals(BigDecimal.valueOf(1000), savedBalanceUser.get(1).getBalance());

        var savedTransaction = argumentCaptorTransaction.getValue();
        Assertions.assertEquals(transactionDto.id(), savedTransaction.getId());
        Assertions.assertEquals(transactionDto.value(), savedTransaction.getValue());
        Assertions.assertEquals(transactionDto.payer(), savedTransaction.getPayer());
        Assertions.assertEquals(transactionDto.receiver(), savedTransaction.getReceiver());

        Assertions.assertEquals("Transação realizada com sucesso.", response);
    }

    @Test
    @DisplayName("Should warn that the user was not found")
    void createTransactionPayerOrReceiverEmpty(){
        UserEntity invalidPayer = new UserEntity();
        UserEntity invalidReceiver = new UserEntity();

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(500),
                invalidPayer,
                invalidReceiver,
                LocalDateTime.now()
        );

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(userRepository.findById(transactionDto.payer().getId())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(transactionDto.receiver().getId())).thenReturn(Optional.empty());

        String response = transactionService.createTransaction(transactionDto);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));

        Assertions.assertEquals("Usuário não encontrado, verifique se o pagador e recebedor estão corretos.", response);
    }

    @Test
    @DisplayName("Should warn that merchants cannot carry out transactions")
    void createTransactionMerchantUnauthorized(){
        UserEntity payerMerchant = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        UserEntity receiverMerchant = new UserEntity(
                2L,
                "RecebedorNome",
                "109.876.543-21",
                "emailcomerciante@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(500),
                payerMerchant,
                receiverMerchant,
                LocalDateTime.now()
        );

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(userRepository.findById(transactionDto.payer().getId())).thenReturn(Optional.of(payerMerchant));
        Mockito.when(userRepository.findById(transactionDto.receiver().getId())).thenReturn(Optional.of(receiverMerchant));

        String response = transactionService.createTransaction(transactionDto);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));

        Assertions.assertEquals("Operação inválida: comerciantes não estão autorizados a realizar transações.", response);
    }

    @Test
    @DisplayName("Should warn that you cannot carry out a transaction for yourself")
    void createTransactionForTheSamePerson(){
        UserEntity payerAndReceiver = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.common,
                BigDecimal.valueOf(500));

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(500),
                payerAndReceiver,
                payerAndReceiver,
                LocalDateTime.now()
        );

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(userRepository.findById(transactionDto.payer().getId())).thenReturn(Optional.of(payerAndReceiver));
        Mockito.when(userRepository.findById(transactionDto.receiver().getId())).thenReturn(Optional.of(payerAndReceiver));

        String response = transactionService.createTransaction(transactionDto);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));

        Assertions.assertEquals("Operação inválida: um usuário não pode realizar transações para si mesmo.", response);
    }

    @Test
    @DisplayName("Should warn that the amount entered is higher than the balance")
    void createTransactionBalanceLowerThanTheAmountStated(){
        UserEntity payerCommon = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.common,
                BigDecimal.valueOf(500));

        UserEntity receiverMerchant = new UserEntity(
                2L,
                "RecebedorNome",
                "109.876.543-21",
                "emailcomerciante@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(1000),
                payerCommon,
                receiverMerchant,
                LocalDateTime.now()
        );

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.OK));
        Mockito.when(userRepository.findById(transactionDto.payer().getId())).thenReturn(Optional.of(payerCommon));
        Mockito.when(userRepository.findById(transactionDto.receiver().getId())).thenReturn(Optional.of(receiverMerchant));

        String response = transactionService.createTransaction(transactionDto);

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));

        Assertions.assertEquals("Operação inválida: o saldo do usuário é menor do que o valor informado.", response);
    }

    @Test
    @DisplayName("Should warn that the Transaction was not authorized")
    void createTransactionNotAuthorized(){
        UserEntity payer = new UserEntity();
        UserEntity receiver = new UserEntity();

        TransactionDto transactionDto = new TransactionDto(
                1L,
                BigDecimal.valueOf(1000),
                payer,
                receiver,
                LocalDateTime.now()
        );

        Mockito.when(authorizationClient.getAuthorization()).thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(TransactionEntity.class));

        String response = transactionService.createTransaction(transactionDto);

        Assertions.assertEquals("Transação não autorizada, tente novamente.", response);
    }

    @Test
    @DisplayName("Should list all successful transactions")
    void listAllTransactionsSuccess(){
        UserEntity payerCommon = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.common,
                BigDecimal.valueOf(500));

        UserEntity receiverMerchant = new UserEntity(
                2L,
                "RecebedorNome",
                "109.876.543-21",
                "emailcomerciante@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        TransactionEntity transaction = new TransactionEntity(
                1L,
                BigDecimal.valueOf(500),
                payerCommon,
                receiverMerchant,
                LocalDateTime.now()
        );

        List<TransactionEntity> listTransactions = new ArrayList<>();
        listTransactions.add(transaction);

        Mockito.when(transactionRepository.findAll()).thenReturn(listTransactions);

        List<TransactionDto> response = transactionService.listAllTransactions();

        Mockito.verify(transactionRepository, Mockito.times(1)).findAll();

        Assertions.assertFalse(listTransactions.isEmpty());
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(1, response.size());

        Assertions.assertEquals(transaction.getId(), response.get(0).id());
        Assertions.assertEquals(transaction.getValue(), response.get(0).value());
        Assertions.assertEquals(transaction.getPayer(), response.get(0).payer());
        Assertions.assertEquals(transaction.getReceiver(), response.get(0).receiver());
    }

    @Test
    @DisplayName("Should list transaction by ID successfully")
    void listTransactionByIdSuccess(){
        UserEntity payerCommon = new UserEntity(
                1L,
                "PagadorNome",
                "123.456.789-10",
                "email@email.com",
                "senha",
                UserEnum.common,
                BigDecimal.valueOf(500));

        UserEntity receiverMerchant = new UserEntity(
                2L,
                "RecebedorNome",
                "109.876.543-21",
                "emailcomerciante@email.com",
                "senha",
                UserEnum.merchant,
                BigDecimal.valueOf(500));

        TransactionEntity transaction = new TransactionEntity(
                1L,
                BigDecimal.valueOf(500),
                payerCommon,
                receiverMerchant,
                LocalDateTime.now()
        );

        Mockito.when(transactionRepository.findById(transaction.getId())).thenReturn(Optional.of(transaction));

        Optional<TransactionDto> response = transactionService.listTransactionById(transaction.getId());

        Mockito.verify(transactionRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assertions.assertEquals(transaction.getId(), response.get().id());
        Assertions.assertEquals(transaction.getValue(), response.get().value());
        Assertions.assertEquals(transaction.getPayer(), response.get().payer());
        Assertions.assertEquals(transaction.getReceiver(), response.get().receiver());
    }

    @Test
    @DisplayName("Should throw exception if the transaction does not exist")
    void listTransactionByIdTransactionEmptyException(){
        Long invalidId = 99L;

        Mockito.when(transactionRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TransactionEmptyException.class, () -> transactionService.listTransactionById(invalidId));

        Mockito.verify(transactionRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }
}
