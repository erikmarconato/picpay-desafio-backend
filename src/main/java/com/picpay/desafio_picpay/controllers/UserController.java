package com.picpay.desafio_picpay.controllers;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Cria um novo usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
            @ApiResponse(responseCode = "409", description = "CPF/CNPJ já cadastrado no sistema"),
            @ApiResponse(responseCode = "415", description = "Tipo de usuário inválido."),
            @ApiResponse(responseCode = "422", description = "Email já cadastrado no sistema."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping
    public ResponseEntity<UserDto> createCommonUser (@RequestBody UserDto userDto){
        return userService.createUser(userDto);
    }


    @Operation(summary = "Lista todos os usuários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todos os usuários."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public List<UserDto> listAllUsers (){
        return userService.listAllUsers();
    }


    @Operation(summary = "Busca o usuário por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao buscar o usuário."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado com o ID em questão."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping("/{id}")
    public Optional<UserDto> listUserById (@PathVariable Long id){
        return userService.listUserById(id);
    }
}
