package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuario")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createCommonUser (@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }

    @GetMapping
    public List<UserDto> listAllUsers (){
        return userService.listAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<UserDto> listUserById (@PathVariable Long id){
        return userService.listUserById(id);
    }
}
