package com.picpay.desafio_picpay.repositories;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserDto> findByDocument(String document);

    Optional<UserDto> findByEmail(String email);

    BigDecimal findByBalance(BigDecimal balance);
}
