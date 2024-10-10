package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.StatusRequestBenefit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record BenefitReqResponseDto (Long id,
                                     UserResponseDto user,
                                     StatusRequestBenefit status,
                                     @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
                                     LocalDateTime dateTime,
                                     BenefitActiveResponseDto benefitActive){

    public static BenefitReqResponseDto toBenefitResponse(BenefitRequest benefitRequest){
        return new BenefitReqResponseDto(
                benefitRequest.getId(),
                UserResponseDto.toUserResponse(benefitRequest.getUser()),
                benefitRequest.getStatus(),
                benefitRequest.getDateTime(),
                BenefitActiveResponseDto.toResponse(benefitRequest.getBenefitActive())
        );
    }

    public static List<BenefitReqResponseDto> toListRequestBenefits(List<BenefitRequest> requests){
        return requests.stream()
                .map(request -> toBenefitResponse(request)).collect(Collectors.toList());
    }
}
