package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.entities.UserEntity;
import com.picpay.desafio_picpay.exceptions.DocumentFoundException;
import com.picpay.desafio_picpay.exceptions.EmailFoundException;
import com.picpay.desafio_picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<UserDto> createCommonUser (UserDto userDto){

        var cpfFound = userRepository.findByDocument(userDto.document());
        var emailFound = userRepository.findByEmail(userDto.email());

        if (cpfFound.isPresent()){
            throw new DocumentFoundException("CPF/CNPJ já cadastrado no sistema");
        }
        if (emailFound.isPresent()){
            throw new EmailFoundException("Email já cadastrado no sistema");
        }

        UserEntity userEntity = new UserEntity(userDto);

        userRepository.save(userEntity);

        var convertEntitytoDto = new UserDto(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getDocument(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getTypeUser()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntitytoDto);
    }
}
