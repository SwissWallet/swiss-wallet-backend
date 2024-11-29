package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserPasswordChangeDto(
                                    @Size(min = 6)
                                    @NotBlank
                                    String currentPassword,
                                    @NotBlank
                                    @Size(min = 6)
                                    String newPassword,
                                    @Size(min = 6)
                                    @NotBlank
                                    String confirmPassword) {
}
