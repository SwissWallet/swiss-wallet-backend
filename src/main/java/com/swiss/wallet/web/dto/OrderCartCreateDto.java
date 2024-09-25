package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.OrderCart;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderCartCreateDto(@NotBlank
                                String username,
                                 List<Long> productIds) {
}
