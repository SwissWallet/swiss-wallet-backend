package com.swiss.wallet.web.dto;
import java.util.List;

public record BenefitGlobalResponseDto(List<BenefitActiveResponseDto> activeResponseDtos,
                                       List<BenefitReqResponseDto> reqResponseDtos) {
}
