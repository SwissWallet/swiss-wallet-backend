package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Product;

import java.util.List;
import java.util.stream.Collectors;

public record ProductResponseDto(Long id,
                                 String name,
                                 float value,
                                 String description,
                                 String category,
                                 String image) {

    public ProductResponseDto(Long id, String name, float value, String description, String category, String image) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.description = description;
        this.category = category;
        this.image = image;
    }

    public static ProductResponseDto toProductResponse(Product product){
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getValue(),
                product.getDescription(),
                product.getCategory().name(),
                product.getImage()
        );
    }

    public static List<ProductResponseDto> toListProductResponse(List<Product> products){
        return products.stream()
                .map(product -> toProductResponse(product)).collect(Collectors.toList());
    }

}
