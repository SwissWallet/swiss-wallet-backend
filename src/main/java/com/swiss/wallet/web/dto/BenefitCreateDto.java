package com.swiss.wallet.web.dto;

import java.time.LocalDateTime;

public record BenefitCreateDto(UserResponseDto user,
                               float value,
                               Long months) {
}
