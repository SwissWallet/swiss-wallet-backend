package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Purchase;

import java.util.List;
import java.util.stream.Collectors;

public record PurchaseResponseDto(Long id,
                                  UserResponseDto user,
                                  List<ProductResponseDto> product,
                                  float value) {

    public PurchaseResponseDto(Long id, UserResponseDto user, List<ProductResponseDto> product, float value) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.value = value;
    }

    public static PurchaseResponseDto toPurchaseResponse(Purchase purchase){
        return new PurchaseResponseDto(
                purchase.getId(),
                UserResponseDto.toUserResponse(purchase.getUser()),
                ProductResponseDto.toListProductResponse(purchase.getProducts()),
                purchase.getValue()
        );
    }

    public static List<PurchaseResponseDto> toListProductResponse(List<Purchase> purchases){
        return purchases.stream()
                .map(purchase -> toPurchaseResponse(purchase)).collect(Collectors.toList());
    }
}
