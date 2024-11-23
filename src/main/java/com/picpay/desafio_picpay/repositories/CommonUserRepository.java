package com.picpay.desafio_picpay.repositories;

import com.picpay.desafio_picpay.dtos.CommonUserDto;
import com.picpay.desafio_picpay.entities.CommonUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUserEntity, Long> {
    Optional<CommonUserDto> findByCpf(String cpf);

    Optional<CommonUserDto> findByEmail(String email);
}
