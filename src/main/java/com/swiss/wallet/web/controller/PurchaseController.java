package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Purchase;
import com.swiss.wallet.service.PurchaseService;
import com.swiss.wallet.web.dto.PurchaseCreateDto;
import com.swiss.wallet.web.dto.PurchaseResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Purchase", description = "Contains all operations related to resources for registering, editing and reading a purchase.")
@RestController
@RequestMapping("/api/v3/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Operation(summary = "Create a new purchase", description = "Feature to create a new purchase",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Insufficient ponint balance",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                     @ApiResponse(responseCode = "404", description = "User or Products not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PurchaseResponseDto> createPurchase(@RequestBody @Valid PurchaseCreateDto purchaseCreateDto) {
        Purchase purchase = purchaseService.savePurchase(purchaseCreateDto);
        return ResponseEntity.ok().body(PurchaseResponseDto.toPurchaseResponse(purchase));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseResponseDto>> listAllPurchase(){
        List<Purchase> purchases = purchaseService.findAll();
        return ResponseEntity.ok().body(PurchaseResponseDto.toListProductResponse(purchases));
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PurchaseResponseDto>> listAllByUser(@RequestParam String username){
        List<Purchase> purchases = purchaseService.findAllByUser(username);
        return ResponseEntity.ok().body(PurchaseResponseDto.toListProductResponse(purchases));
    }
}
