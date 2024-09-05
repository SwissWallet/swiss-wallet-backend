package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public record OrderResponseDto(Long id,
                               UserResponseDto user,
                               ProductResponseDto product,
                               String status) {

    public OrderResponseDto(Long id, UserResponseDto user, ProductResponseDto product, String status) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.status = status;
    }

    public static OrderResponseDto toOrderResponse(Order order){
        return new OrderResponseDto(
                order.getId(),
                UserResponseDto.toUserResponse(order.getUser()),
                ProductResponseDto.toProductResponse(order.getProduct()),
                order.getStatus().name()
        );
    }

    public static List<OrderResponseDto> toListOrderResponse(List<Order> orders){
        return orders.stream()
                .map(order -> toOrderResponse(order)).collect(Collectors.toList());
    }
}

