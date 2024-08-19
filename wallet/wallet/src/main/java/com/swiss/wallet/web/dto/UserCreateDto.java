package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;

public record UserCreateDto (@NotBlank
                             @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
                             String username,

                             @NotBlank
                             @Min(value = 6)
                             String password,

                             @NotBlank
                             String name,

                             @CPF
                             @NotBlank
                             String cpf,

                             @NotBlank
                             String birthDate,

                             @Size(min = 11, max = 11)
                             String phone){

    public UserEntity toUser() {
        return new UserEntity(
                username,
                password,
                name,
                cpf,
                birthDate,
                phone
        );
    }
}
