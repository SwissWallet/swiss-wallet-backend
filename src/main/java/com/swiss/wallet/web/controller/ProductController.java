package com.swiss.wallet.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.service.ProductService;
import com.swiss.wallet.web.dto.ProductCreateDto;
import com.swiss.wallet.web.dto.ProductResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Products", description = "Contains all operations related to resources for registering, editing and reading a product.")
@RestController
@RequestMapping("/api/v3/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Create a new product", description = "Feature to create a new product",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
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
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
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
