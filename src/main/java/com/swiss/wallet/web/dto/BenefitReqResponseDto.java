package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.StatusBenefit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BenefitReqResponseDto (Long id,
                                     UserResponseDto user,
                                     StatusBenefit status,
                                     @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
                                     LocalDateTime dateTime,
                                     String description){

    public BenefitReqResponseDto(Long id, UserResponseDto user, StatusBenefit status, LocalDateTime dateTime, String description) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.dateTime = dateTime;
        this.description = description;
    }

    public static BenefitReqResponseDto toBenefitResponse(BenefitRequest benefitRequest){
        return new BenefitReqResponseDto(
                benefitRequest.getId(),
                UserResponseDto.toUserResponse(benefitRequest.getUser()),
                benefitRequest.getStatus(),
                benefitRequest.getDateTime(),
                benefitRequest.getDescription()
        );
    }

    public static List<BenefitReqResponseDto> toListRequestBenefits(List<BenefitRequest> requests){
        return requests.stream()
                .map(request -> toBenefitResponse(request)).collect(Collectors.toList());
    }
}
