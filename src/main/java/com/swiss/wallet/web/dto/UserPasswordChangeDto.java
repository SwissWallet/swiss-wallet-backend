package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;

public record UserPasswordChangeDto(@NotBlank
                                    String currentPassword,
                                    @NotBlank
                                    String newPassword,
                                    @NotBlank
                                    String confirmPassword) {
}
