package com.picpay.desafio_picpay.entities;

import com.picpay.desafio_picpay.dtos.UserDto;
import com.picpay.desafio_picpay.enums.UserEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

    @Column (name = "cpf_cnpj", nullable = false, unique = true)
    private String document;

    @Column (name = "email", nullable = false, unique = true)
    private String email;

    @Column (name = "senha", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column (name = "tipo_usuario", nullable = false)
    private UserEnum typeUser;

    @Column (name = "saldo")
    private BigDecimal balance = BigDecimal.valueOf(0);

    public UserEntity(UserDto userDto) {
        this.id = userDto.id();
        this.fullName = userDto.fullName();
        this.document = userDto.document();
        this.email = userDto.email();
        this.password = userDto.password();
        this.typeUser = userDto.typeUser();
        this.balance = userDto.balance();
    }
}
