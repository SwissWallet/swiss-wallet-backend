package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.ExtractService;
import com.swiss.wallet.web.dto.ExtractResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Extracts", description = "Contains all operations related to resources for registering, editing and reading a extract.")
@RestController
@RequestMapping("/api/v3/extracts")
public class ExtractController {

    private final ExtractService extractService;

    public ExtractController(ExtractService extractService) {
        this.extractService = extractService;
    }

    @Operation(summary = "Recover extract logged in user", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<?> findExtractByUserLogged(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<Extract> extract = extractService.findByUserLogged(userDetails.getId());
        if (extract == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(ExtractResponseDto.toListExtractResponse(extract));
    }
}
