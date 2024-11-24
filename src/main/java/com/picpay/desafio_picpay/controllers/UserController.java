package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario_comum")
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createCommonUser (@RequestBody UserDto userDto){
        return userService.createCommonUser(userDto);
    }
}
