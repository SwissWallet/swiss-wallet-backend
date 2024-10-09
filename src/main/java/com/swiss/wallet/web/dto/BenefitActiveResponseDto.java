package com.swiss.wallet.web.dto;
import com.swiss.wallet.entity.BenefitActive;
import java.util.List;
import java.util.stream.Collectors;

public record BenefitActiveResponseDto(Long id,
                                       String title,
                                       String description) {

    public static BenefitActiveResponseDto toResponse(BenefitActive benefit){
        return new BenefitActiveResponseDto(
                benefit.getId(),
                benefit.getTitle(),
                benefit.getDescription()
        );
    }

    public static List<BenefitActiveResponseDto> toListBenefitrResponse(List<BenefitActive> benefits){
        return benefits.stream()
                .map(benefit -> toResponse(benefit)).collect(Collectors.toList());
    }


}
