package com.picpay.desafio_picpay.entities;

import com.picpay.desafio_picpay.dtos.TransactionDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table (name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "valor")
    private BigDecimal value = BigDecimal.valueOf(0);

    @ManyToOne
    @JoinColumn (name = "pagador_id")
    private UserEntity payer;

    @ManyToOne
    @JoinColumn (name = "recebedor_id")
    private UserEntity receiver;

    @Column (name = "horario_transacao")
    private LocalDateTime transactionTime;

    public TransactionEntity(TransactionDto transactionDto) {
        this.id = transactionDto.id();
        this.value = transactionDto.value();
        this.payer = transactionDto.payer();
        this.receiver = transactionDto.receiver();
        this.transactionTime = transactionDto.transactionTime();
    }


    @PrePersist
    private void onTransaction(){
        this.transactionTime = LocalDateTime.now();
    }
}
