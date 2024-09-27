package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.OrderCart;

import java.util.List;
import java.util.stream.Collectors;

public record OrderCartResponseDto(Long id,
                                   UserResponseDto user,
                                   List<ProductResponseDto> product,
                                   float value,
                                   String status) {


    public static OrderCartResponseDto toPurchaseResponse(OrderCart orderCart){
        return new OrderCartResponseDto(
                orderCart.getId(),
                UserResponseDto.toUserResponse(orderCart.getUser()),
                ProductResponseDto.toListProductResponse(orderCart.getProducts()),
                orderCart.getValue(),
                orderCart.getStatus().name()
        );
    }

    public static List<OrderCartResponseDto> toListProductResponse(List<OrderCart> orderCarts){
        return orderCarts.stream()
                .map(orderCart -> toPurchaseResponse(orderCart)).collect(Collectors.toList());
    }
}
