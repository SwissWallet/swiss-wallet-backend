package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.Favorite;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.FavoriteService;
import com.swiss.wallet.web.dto.FavoriteResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorite", description = "Contains all operations related to resources for registering, editing and reading a favorite product.")
@RestController
@RequestMapping("/api/v3/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @Operation(summary = "Save a new favorite product", description = "Feature to save a new favorite product",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource saved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Resource not processed due to existing favorite",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> saveFavorite(@AuthenticationPrincipal JwtUserDetails userDetails,
                                             @RequestParam("idProduct") Long idProduct){
        favoriteService.saveFavorite(userDetails.getId(), idProduct);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<FavoriteResponseDto>> findAllByUserCurrent(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<Favorite> favorites = favoriteService.findAllByUser(userDetails.getId());
        return ResponseEntity.ok().body(FavoriteResponseDto.toListFavoriteResponse(favorites));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> removeFavorite(@AuthenticationPrincipal JwtUserDetails userDetails,
                                               @RequestParam("idProduct") Long idProduct){
        favoriteService.deleteFavorite(userDetails.getId(), idProduct);
        return ResponseEntity.ok().build();
    }
}
