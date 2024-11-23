package com.picpay.desafio_picpay.entities;

import com.picpay.desafio_picpay.dtos.CommonUserDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuario_comum")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Long id;

    @Column (name = "nome_completo", nullable = false)
    private String fullName;

    @Column (name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column (name = "email", nullable = false, unique = true)
    private String email;

    @Column (name = "senha", nullable = false)
    private String password;

    public CommonUserEntity(CommonUserDto commonUserDto) {
        this.id = commonUserDto.id();
        this.fullName = commonUserDto.fullName();
        this.cpf = commonUserDto.cpf();
        this.email = commonUserDto.email();
        this.password = commonUserDto.password();
    }
}
