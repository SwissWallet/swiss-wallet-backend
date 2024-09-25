package com.swiss.wallet.web.dto;

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
                                 LocalDateTime expireDate) {

    public BenefitResponseDto(Long id, UserResponseDto user, float value, String statusBenefit, LocalDateTime expireDate) {
        this.id = id;
        this.user = user;
        this.value = value;
        this.statusBenefit = statusBenefit;
        this.expireDate = expireDate;
    }

    public static BenefitResponseDto toBenefitResponse(Benefit benefit){
        return new BenefitResponseDto(
                benefit.getId(),
                UserResponseDto.toUserResponse(benefit.getUser()),
                benefit.getValue(),
                benefit.getStatusBenefit().name(),
                benefit.getExpireDate()
        );
    }

    public static List<BenefitResponseDto> toListBenefitrResponse(List<Benefit> benefits){
        return benefits.stream()
                .map(benefit -> toBenefitResponse(benefit)).collect(Collectors.toList());
    }
}
