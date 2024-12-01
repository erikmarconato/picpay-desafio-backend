package com.picpay.desafio_picpay.services;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.entities.UserEntity;
import com.picpay.desafio_picpay.enums.UserEnum;
import com.picpay.desafio_picpay.exceptions.DocumentFoundException;
import com.picpay.desafio_picpay.exceptions.EmailFoundException;
import com.picpay.desafio_picpay.exceptions.UserEmptyException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Should successfully create a user")
    void createUserSuccess(){
        UserDto userCommonDto = new UserDto(
                1L,
                "NomeCompleto",
                "123.456.789-10",
                "Email@Email.com",
                "Senha",
                UserEnum.common,
                BigDecimal.valueOf(1000));

        UserEntity userCommonEntity = new UserEntity();
        userCommonEntity.setId(userCommonDto.id());
        userCommonEntity.setFullName(userCommonDto.fullName());
        userCommonEntity.setDocument(userCommonDto.document());
        userCommonEntity.setEmail(userCommonDto.email());
        userCommonEntity.setPassword(userCommonDto.password());
        userCommonEntity.setTypeUser(userCommonDto.typeUser());
        userCommonEntity.setBalance(userCommonDto.balance());

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        Mockito.when(userRepository.findByDocument(userCommonDto.document())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(userCommonDto.email())).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userService.createUser(userCommonDto);

        Mockito.verify(userRepository, Mockito.times(1)).save(argumentCaptor.capture());

        UserEntity savedUser = argumentCaptor.getValue();
        Assertions.assertEquals(userCommonEntity.getFullName(), savedUser.getFullName());
        Assertions.assertEquals(userCommonEntity.getDocument(), savedUser.getDocument());
        Assertions.assertEquals(userCommonEntity.getEmail(), savedUser.getEmail());
        Assertions.assertEquals(userCommonEntity.getPassword(), savedUser.getPassword());
        Assertions.assertEquals(userCommonEntity.getTypeUser(), savedUser.getTypeUser());
        Assertions.assertEquals(userCommonEntity.getBalance(), savedUser.getBalance());

        UserDto responseBody = response.getBody();
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(userCommonDto.id(), responseBody.id());
        Assertions.assertEquals(userCommonDto.fullName(), responseBody.fullName());
        Assertions.assertEquals(userCommonDto.document(), responseBody.document());
        Assertions.assertEquals(userCommonDto.email(), responseBody.email());
        Assertions.assertEquals(userCommonDto.password(), responseBody.password());
        Assertions.assertEquals(userCommonDto.typeUser(), responseBody.typeUser());
        Assertions.assertEquals(userCommonDto.balance(), responseBody.balance());
    }

    @Test
    @DisplayName("Should exception must be thrown if the document already exists in the system")
    void createUserDocumentFoundException(){
        UserDto userCommonDto = new UserDto(
                1L,
                "NomeCompleto",
                "123.456.789-10",
                "Email@Email.com",
                "Senha",
                UserEnum.common,
                BigDecimal.valueOf(1000));

        Mockito.when(userRepository.findByDocument(userCommonDto.document())).thenReturn(Optional.of(new UserEntity()));

        Assertions.assertThrows(DocumentFoundException.class, () -> userService.createUser(userCommonDto));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("Should exception must be thrown if the email already exists in the system")
    void createUserEmailFoundException(){
        UserDto userCommonDto = new UserDto(
                1L,
                "NomeCompleto",
                "123.456.789-10",
                "Email@Email.com",
                "Senha",
                UserEnum.common,
                BigDecimal.valueOf(1000));

        Mockito.when(userRepository.findByDocument(userCommonDto.document())).thenReturn(Optional.empty());
        Mockito.when(userRepository.findByEmail(userCommonDto.email())).thenReturn(Optional.of(new UserEntity()));
        Assertions.assertThrows(EmailFoundException.class, () -> userService.createUser(userCommonDto));

        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(UserEntity.class));
    }

    @Test
    @DisplayName("Should list all successful users")
    void listAllUsersSuccess(){
        UserEntity userCommonEntity = new UserEntity(
                1L,
                "NomeCompleto",
                "123.456.789-10",
                "Email@Email.com",
                "Senha",
                UserEnum.common,
                BigDecimal.valueOf(1000)
        );

        List<UserEntity> userList = new ArrayList<>();
        userList.add(userCommonEntity);

        Mockito.when(userRepository.findAll()).thenReturn(userList);

        List<UserDto> response = userService.listAllUsers();

        Mockito.verify(userRepository, Mockito.times(1)).findAll();

        Assertions.assertFalse(userList.isEmpty());
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(1, response.size());

        Assertions.assertEquals(userCommonEntity.getId(), userList.get(0).getId());
        Assertions.assertEquals(userCommonEntity.getFullName(), userList.get(0).getFullName());
        Assertions.assertEquals(userCommonEntity.getDocument(), userList.get(0).getDocument());
        Assertions.assertEquals(userCommonEntity.getEmail(), userList.get(0).getEmail());
        Assertions.assertEquals(userCommonEntity.getPassword(), userList.get(0).getPassword());
        Assertions.assertEquals(userCommonEntity.getTypeUser(), userList.get(0).getTypeUser());
        Assertions.assertEquals(userCommonEntity.getBalance(), userList.get(0).getBalance());
    }

    @Test
    @DisplayName("Should list user by id successfully")
    void listUserByIdSuccess(){
        UserEntity userCommonEntity = new UserEntity(
                1L,
                "NomeCompleto",
                "123.456.789-10",
                "Email@Email.com",
                "Senha",
                UserEnum.common,
                BigDecimal.valueOf(1000)
        );

        Mockito.when(userRepository.findById(userCommonEntity.getId())).thenReturn(Optional.of(userCommonEntity));

        Optional<UserDto> response = userService.listUserById(userCommonEntity.getId());

        Mockito.verify(userRepository, Mockito.times(1)).findById(userCommonEntity.getId());

        Assertions.assertTrue(response.isPresent());

        Assertions.assertEquals(userCommonEntity.getId(), response.get().id());
        Assertions.assertEquals(userCommonEntity.getFullName(), response.get().fullName());
        Assertions.assertEquals(userCommonEntity.getDocument(), response.get().document());
        Assertions.assertEquals(userCommonEntity.getEmail(), response.get().email());
        Assertions.assertEquals(userCommonEntity.getPassword(), response.get().password());
        Assertions.assertEquals(userCommonEntity.getTypeUser(), response.get().typeUser());
        Assertions.assertEquals(userCommonEntity.getBalance(), response.get().balance());
    }

    @Test
    @DisplayName("Should throw exception if the user does not exist")
    void listUserByIdUserEmptyException(){
        Long invalidId = 99L;

        Mockito.when(userRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(UserEmptyException.class, () -> userService.listUserById(invalidId));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }
}
