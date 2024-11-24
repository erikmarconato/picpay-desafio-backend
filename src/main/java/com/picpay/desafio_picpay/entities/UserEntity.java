package com.picpay.desafio_picpay.entities;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.enums.UserEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "nome_completo", nullable = false)
    private String fullName;

    @Column (name = "cpf/cnpj", nullable = false, unique = true)
    private String document;

    @Column (name = "email", nullable = false, unique = true)
    private String email;

    @Column (name = "senha", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column (name = "tipo_usuario", nullable = false)
    private UserEnum typeUser;

    public UserEntity(UserDto userDto) {
        this.id = userDto.id();
        this.fullName = userDto.fullName();
        this.document = userDto.document();
        this.email = userDto.email();
        this.password = userDto.password();
        this.typeUser = userDto.typeUser();
    }
}
