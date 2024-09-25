package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Benefit;
import com.swiss.wallet.service.BenefitService;
import com.swiss.wallet.web.dto.BenefitCreateDto;
import com.swiss.wallet.web.dto.BenefitResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v3/benefits")
public class BenefitController {

    private final BenefitService benefitService;

    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BenefitResponseDto> saveBenefit(@RequestBody BenefitCreateDto dto){
        Benefit benefit = benefitService.createBenefit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BenefitResponseDto.toBenefitResponse(benefit));
    }


}
