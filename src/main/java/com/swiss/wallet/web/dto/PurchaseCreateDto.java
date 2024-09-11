package com.swiss.wallet.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PurchaseCreateDto(@NotBlank
                                String userUsername,
                                List<Long> productIds) {
}
