package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.CommonUserDto;
import com.picpay.desafio_picpay.services.CommonUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario_comum")
@CrossOrigin
public class CommonUserController {

    @Autowired
    CommonUserService commonUserService;

    @PostMapping
    public ResponseEntity<CommonUserDto> createCommonUser (@RequestBody CommonUserDto commonUserDto){
        return commonUserService.createCommonUser(commonUserDto);
    }
}
