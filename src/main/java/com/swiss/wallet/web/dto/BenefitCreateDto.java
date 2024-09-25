package com.swiss.wallet.web.dto;

import java.time.LocalDateTime;

public record BenefitCreateDto(Long userId,
                               float value,
                               Long months,
                               String description) {
}
