package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginDto(@NotBlank
                           @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
                           String username,
                           @NotBlank
                           @Size(min = 6)
                           String password) {
}
