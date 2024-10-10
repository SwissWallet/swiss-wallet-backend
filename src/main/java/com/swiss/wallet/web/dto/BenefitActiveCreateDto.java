package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;

public record BenefitActiveCreateDto (@NotBlank
                                      String title,
                                      @NotBlank
                                      String description) {
}
