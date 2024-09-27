package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Benefit;
import com.swiss.wallet.service.BenefitService;
import com.swiss.wallet.web.dto.BenefitCreateDto;
import com.swiss.wallet.web.dto.BenefitResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Tag(name = "Benefit", description = "Contains all operations related to resources for registering, editing and reading a benefit.")
@RestController
@RequestMapping("/api/v3/benefits")
public class BenefitController {

    private final BenefitService benefitService;

    public BenefitController(BenefitService benefitService) {
        this.benefitService = benefitService;
    }

    @Operation(summary = "Create a new Benefit", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BenefitResponseDto> saveBenefit(@RequestBody BenefitCreateDto dto){
        Benefit benefit = benefitService.createBenefit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BenefitResponseDto.toBenefitResponse(benefit));
    }


    @PutMapping("/disable/{idBenefit}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> disableBenefit(@PathVariable Long idBenefit){
        benefitService.disableBenefit(idBenefit);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Search all benefit", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "204", description = "Resource retrieved successfully, list is empty",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BenefitResponseDto>> listAll(){
        List<Benefit> benefits = benefitService.getAll();
        if (benefits.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(BenefitResponseDto.toListBenefitrResponse(benefits));
    }

    @GetMapping("/{idBenefit}")
    public ResponseEntity<BenefitResponseDto> findById(@PathVariable Long idBenefit){
        Benefit benefit = benefitService.getById(idBenefit);
        return ResponseEntity.ok().body(BenefitResponseDto.toBenefitResponse(benefit));
    }



}
