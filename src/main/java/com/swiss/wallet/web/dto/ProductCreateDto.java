package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductCreateDto(@NotBlank
                                String name,
                               @NotNull
                                float value,
                               @NotBlank
                                String description,
                               @NotBlank
                                String category){
}
