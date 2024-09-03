package com.swiss.wallet.service;

import com.swiss.wallet.entity.Category;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IProductRepository;
import com.swiss.wallet.web.dto.ProductCreateDto;
import com.swiss.wallet.web.dto.ProductResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponseDto saveProduct(ProductCreateDto createDto, MultipartFile file) {
        Product product = new Product();
        product.setName(createDto.name());
        product.setDescription(createDto.description());

        // Set category based on the DTO
        switch (createDto.category()) {
            case "STORE" -> product.setCategory(Category.STORE);
            case "CANTEEN" -> product.setCategory(Category.CANTEEN);
            case "LIBRARY" -> product.setCategory(Category.LIBRARY);
        }

        product.setValue(createDto.value());

        try {
            // Encode the image without resizing
            String encodedImage = encodeImageToBase64(file);
            product.setImage(encodedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image", e);
        }

        // Save the product to the repository
        productRepository.save(product);

        // Convert to DTO for response
        return ProductResponseDto.toProductResponse(product);
    }

    private String encodeImageToBase64(MultipartFile file) throws IOException {
        // Read the original image
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());

        // Convert the image to a byte array and encode it in Base64
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream); // Save as PNG to avoid quality loss
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    public List<Product> findAllByCategory(String category) {

        switch (category) {
            case "STORE" -> {
                return productRepository.findAllByCategory(Category.STORE);
            }
            case "CANTEEN" -> {
                return productRepository.findAllByCategory(Category.CANTEEN);
            }
            case "LIBRARY" -> {
                return productRepository.findAllByCategory(Category.LIBRARY);
            }
        }
        return null;
    }

    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException("Product not found, Please check the product ID and try again")
                );
        productRepository.deleteById(product.getId());
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
