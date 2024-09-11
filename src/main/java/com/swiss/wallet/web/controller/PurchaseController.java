package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Purchase;
import com.swiss.wallet.service.PurchaseService;
import com.swiss.wallet.web.dto.PurchaseCreateDto;
import com.swiss.wallet.web.dto.PurchaseResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseResponseDto> createPurchase(@RequestBody @Valid PurchaseCreateDto purchaseCreateDto) {
        Purchase purchase = purchaseService.savePurchase(purchaseCreateDto);
        return ResponseEntity.ok().body(PurchaseResponseDto.toPurchaseResponse(purchase));
    }
}
