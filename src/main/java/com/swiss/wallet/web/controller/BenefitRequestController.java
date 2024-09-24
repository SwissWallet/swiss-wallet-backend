package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.BenefitRequestService;
import com.swiss.wallet.web.dto.BenefitReqCreateDto;
import com.swiss.wallet.web.dto.BenefitReqResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/benefit-requests")
public class BenefitRequestController {

    private final BenefitRequestService benefitRequestService;

    public BenefitRequestController(BenefitRequestService benefitRequestService) {
        this.benefitRequestService = benefitRequestService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BenefitReqResponseDto> saveBenefitRequest(
            @RequestBody BenefitReqCreateDto dto,
            @AuthenticationPrincipal JwtUserDetails userDetails){
        BenefitRequest benefitRequest = benefitRequestService.createRequestBenefit(userDetails.getId(), dto.description());
        return ResponseEntity.status(HttpStatus.CREATED).body(BenefitReqResponseDto.toBenefitResponse(benefitRequest));
    }
}