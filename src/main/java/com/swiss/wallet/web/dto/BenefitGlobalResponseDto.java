package com.swiss.wallet.web.dto;
import java.util.List;

public class BenefitGlobalResponseDto
{

    private List<BenefitActiveResponseDto> activeResponseDtos;
    private List<BenefitReqResponseDto> reqResponseDtos;

    public BenefitGlobalResponseDto(List<BenefitActiveResponseDto> activeResponseDtos, List<BenefitReqResponseDto> reqResponseDtos) {
        this.activeResponseDtos = activeResponseDtos;
        this.reqResponseDtos = reqResponseDtos;
    }

    public BenefitGlobalResponseDto() {
    }

    public List<BenefitActiveResponseDto> getActiveResponseDtos() {
        return activeResponseDtos;
    }

    public void setActiveResponseDtos(List<BenefitActiveResponseDto> activeResponseDtos) {
        this.activeResponseDtos = activeResponseDtos;
    }

    public List<BenefitReqResponseDto> getReqResponseDtos() {
        return reqResponseDtos;
    }

    public void setReqResponseDtos(List<BenefitReqResponseDto> reqResponseDtos) {
        this.reqResponseDtos = reqResponseDtos;
    }
}
