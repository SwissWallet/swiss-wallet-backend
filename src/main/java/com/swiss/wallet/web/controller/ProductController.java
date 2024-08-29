package com.swiss.wallet.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.service.ProductService;
import com.swiss.wallet.web.dto.ProductCreateDto;
import com.swiss.wallet.web.dto.ProductResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v3/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> saveProduct(
            @RequestParam("createDto") String createJson,
            @RequestParam("image") MultipartFile file){
        ObjectMapper objectMapper = new ObjectMapper();
        ProductCreateDto createDto = null;
        try {
            createDto = objectMapper.readValue(createJson, ProductCreateDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        ProductResponseDto responseDto = productService.saveProduct(createDto, file);
        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/category")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('CLIENT')")
    public ResponseEntity<List<ProductResponseDto>> filterProduct(
            @RequestParam String category
    ){
        List<Product> products = productService.findAllByCategory(category);
        return ResponseEntity.ok().body(ProductResponseDto.toListProductResponse(products));
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@RequestParam Long id){
        productService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
