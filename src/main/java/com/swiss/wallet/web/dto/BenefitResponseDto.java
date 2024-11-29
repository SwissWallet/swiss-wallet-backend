package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.entity.Benefit;
import com.swiss.wallet.entity.StatusBenefit;
import com.swiss.wallet.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BenefitResponseDto(Long id,
                                 UserResponseDto user,
                                 float value,
                                 String statusBenefit,
                                 @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
                                 LocalDateTime expireDate,
                                 BenefitActiveResponseDto benefitActiveResponseDt) {

    public static BenefitResponseDto toBenefitResponse(Benefit benefit){
        return new BenefitResponseDto(
                benefit.getId(),
                UserResponseDto.toUserResponse(benefit.getUser()),
                benefit.getValue(),
                benefit.getStatusBenefit().name(),
                benefit.getExpireDate(),
                BenefitActiveResponseDto.toResponse(benefit.getBenefitActive())
        );
    }

    public static List<BenefitResponseDto> toListBenefitrResponse(List<Benefit> benefits){
        return benefits.stream()
                .map(benefit -> toBenefitResponse(benefit)).collect(Collectors.toList());
    }
}
