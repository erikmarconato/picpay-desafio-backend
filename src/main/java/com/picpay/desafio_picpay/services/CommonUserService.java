package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.dtos.CommonUserDto;
import com.picpay.desafio_picpay.entities.CommonUserEntity;
import com.picpay.desafio_picpay.repositories.CommonUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CommonUserService {

    @Autowired
    CommonUserRepository commonUserRepository;

    public ResponseEntity<CommonUserDto> createCommonUser (CommonUserDto commonUserDto){

        CommonUserEntity commonUserEntity = new CommonUserEntity(commonUserDto);

        commonUserRepository.save(commonUserEntity);

        var convertEntitytoDto = new CommonUserDto(
                commonUserEntity.getId(),
                commonUserEntity.getFullName(),
                commonUserEntity.getCpf(),
                commonUserEntity.getEmail(),
                commonUserEntity.getPassword()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntitytoDto);
    }
}
