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
                                 String description) {

    public BenefitResponseDto(Long id, UserResponseDto user, float value, String statusBenefit, LocalDateTime expireDate, String description) {
        this.id = id;
        this.user = user;
        this.value = value;
        this.statusBenefit = statusBenefit;
        this.expireDate = expireDate;
        this.description = description;
    }

    public static BenefitResponseDto toBenefitResponse(Benefit benefit){
        return new BenefitResponseDto(
                benefit.getId(),
                UserResponseDto.toUserResponse(benefit.getUser()),
                benefit.getValue(),
                benefit.getStatusBenefit().name(),
                benefit.getExpireDate(),
                benefit.getDescription()
        );
    }

    public static List<BenefitResponseDto> toListBenefitrResponse(List<Benefit> benefits){
        return benefits.stream()
                .map(benefit -> toBenefitResponse(benefit)).collect(Collectors.toList());
    }
}
