package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Favorite;

import java.util.List;
import java.util.stream.Collectors;

public record FavoriteResponseDto(Long id,
                                  UserResponseDto user,
                                  ProductResponseDto product) {

    public static FavoriteResponseDto toFavoriteResponse(Favorite favorite){
        return new FavoriteResponseDto(
                favorite.getId(),
                UserResponseDto.toUserResponse(favorite.getUser()),
                ProductResponseDto.toProductResponse(favorite.getProduct())
        );
    }

    public static List<FavoriteResponseDto> toListFavoriteResponse(List<Favorite> favorites){
        return favorites.stream()
                .map(favorite -> toFavoriteResponse(favorite)).collect(Collectors.toList());
    }
}

