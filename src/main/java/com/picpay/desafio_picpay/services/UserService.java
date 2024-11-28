package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.entities.UserEntity;
import com.picpay.desafio_picpay.exceptions.DocumentFoundException;
import com.picpay.desafio_picpay.exceptions.EmailFoundException;
import com.picpay.desafio_picpay.exceptions.UserEmptyException;
import com.picpay.desafio_picpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<UserDto> createUser (UserDto userDto){

        var cpfFound = userRepository.findByDocument(userDto.document());
        var emailFound = userRepository.findByEmail(userDto.email());

        if (cpfFound.isPresent()){
            throw new DocumentFoundException("CPF/CNPJ já cadastrado no sistema.");
        }
        if (emailFound.isPresent()){
            throw new EmailFoundException("Email já cadastrado no sistema.");
        }

        UserEntity userEntity = new UserEntity(userDto);

        userRepository.save(userEntity);

        var convertEntitytoDto = new UserDto(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getDocument(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getTypeUser(),
                userEntity.getBalance()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(convertEntitytoDto);
    }

    public List<UserDto> listAllUsers (){

        List<UserEntity> users = userRepository.findAll();

        return users.stream().map(userEntity -> new UserDto(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getDocument(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getTypeUser(),
                userEntity.getBalance()
        )).toList();
    }

    public Optional<UserDto> listUserById (Long id){

        Optional<UserEntity> user = userRepository.findById(id);

        if (user.isEmpty()){
            throw new UserEmptyException("Usuário com o id: " + id + " não encontrado.");
        }

        return user.map(userEntity -> new UserDto(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getDocument(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getTypeUser(),
                userEntity.getBalance()
        ));
    }
}
