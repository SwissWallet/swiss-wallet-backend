package com.swiss.wallet.web.controller;

import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> saveFavorite(@AuthenticationPrincipal JwtUserDetails userDetails,
                                             @RequestParam("idProduct") Long idProduct){
        favoriteService.saveFavorite(userDetails.getId(), idProduct);
        return ResponseEntity.ok().build();
    }

}
