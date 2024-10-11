package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.BenefitRequestService;
import com.swiss.wallet.web.dto.*;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Create a new Benefit Request", description = "Request requires a Bearer Token. Restricted access to CLIENT",
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
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BenefitReqResponseDto> saveBenefitRequest(
            @RequestBody BenefitReqCreateDto dto,
            @AuthenticationPrincipal JwtUserDetails userDetails){
        BenefitRequest benefitRequest = benefitRequestService.createRequestBenefit(userDetails.getId(), dto.idBenefit());
        return ResponseEntity.status(HttpStatus.CREATED).body(BenefitReqResponseDto.toBenefitResponse(benefitRequest));
    }

    @Operation(summary = "Search all benefit request", description = "Request requires a Bearer Token. Restricted access to ADMIN",
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
    public ResponseEntity<List<BenefitReqResponseDto>> listAll(){
        List<BenefitRequest> requests = benefitRequestService.getAll();
        List<BenefitReqResponseDto> reqResponseDtos = BenefitReqResponseDto.toListRequestBenefits(requests);
        if (!requests.isEmpty()){
            return ResponseEntity.ok().body(reqResponseDtos);
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Change status benefit request", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatus(@RequestBody UpdateStatusBenefitReqDto dto){
        benefitRequestService.updateStatus(dto.idBenefit(), dto.status());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete benefit request by id", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeBenefitRequest(@PathVariable Long id){
        benefitRequestService.removeBenefitRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<BenefitGlobalResponseDto> currentBenefitReq(@AuthenticationPrincipal JwtUserDetails userDetails){
        BenefitGlobalResponseDto benefitGlobalResponseDto = benefitRequestService.getAllByUser(userDetails.getId());

        return ResponseEntity.ok().body(benefitGlobalResponseDto);
    }
}
