package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.BenefitActive;
import com.swiss.wallet.service.BenefitActiveService;
import com.swiss.wallet.web.dto.BenefitActiveCreateDto;
import com.swiss.wallet.web.dto.BenefitActiveResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/benefit/actives")
public class BenefitActiveController {

    private final BenefitActiveService activeService;

    public BenefitActiveController(BenefitActiveService activeService) {
        this.activeService = activeService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BenefitActiveResponseDto> saveBenefit(@RequestBody @Valid BenefitActiveCreateDto dto){
        BenefitActive benefitActive = activeService.createBenefit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BenefitActiveResponseDto.toResponse(benefitActive));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('CLIENT')")
    public ResponseEntity<List<BenefitActiveResponseDto>> listAll(){
        List<BenefitActive> benefitActives = activeService.listAll();
        return ResponseEntity.ok().body(BenefitActiveResponseDto.toListBenefitrResponse(benefitActives));
    }

}
