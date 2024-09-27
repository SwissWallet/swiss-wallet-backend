package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.BenefitRequestService;
import com.swiss.wallet.web.dto.BenefitReqCreateDto;
import com.swiss.wallet.web.dto.BenefitReqResponseDto;
import com.swiss.wallet.web.dto.UpdateStatusBenefitReqDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Benefit Request", description = "Contains all operations related to resources for registering, editing and reading a benefit request.")
@RestController
@RequestMapping("/api/v3/benefit/requests")
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BenefitReqResponseDto>> listAll(){
        List<BenefitRequest> requests = benefitRequestService.getAll();
        List<BenefitReqResponseDto> reqResponseDtos = BenefitReqResponseDto.toListRequestBenefits(requests);
        if (!requests.isEmpty()){
            return ResponseEntity.ok().body(reqResponseDtos);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatus(@RequestBody UpdateStatusBenefitReqDto dto){
        benefitRequestService.updateStatus(dto.idBenefit(), dto.status());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeBenefitRequest(@PathVariable Long id){
        benefitRequestService.removeBenefitRequest(id);
        return ResponseEntity.ok().build();
    }
}
