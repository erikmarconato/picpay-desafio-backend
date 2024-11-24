package com.picpay.desafio_picpay.dtos;

import com.picpay.desafio_picpay.enums.UserEnum;

public record UserDto(
        Long id,
        String fullName,
        String document,
        String email,
        String password,
        UserEnum typeUser) {
}
