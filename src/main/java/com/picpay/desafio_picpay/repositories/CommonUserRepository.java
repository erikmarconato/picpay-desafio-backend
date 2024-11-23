package com.picpay.desafio_picpay.repositories;

import com.picpay.desafio_picpay.entities.CommonUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonUserRepository extends JpaRepository<CommonUserEntity, Long> {
}
