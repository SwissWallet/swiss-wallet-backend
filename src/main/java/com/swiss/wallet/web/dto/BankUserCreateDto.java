package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record BankUserCreateDto(@NotBlank
                            String username,
                                @NotBlank
                            String name,
                                @CPF
                            String cpf,
                                @NotBlank
                            String phone,
                                @NotBlank
                            String password) {
}
