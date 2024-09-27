package com.swiss.wallet.service;

import com.swiss.wallet.entity.Category;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.StatusProduct;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IFavoriteRepository;
import com.swiss.wallet.repository.IOrderRepository;
import com.swiss.wallet.repository.IProductRepository;
import com.swiss.wallet.web.dto.ChangeProductDto;
import com.swiss.wallet.web.dto.ProductCreateDto;
import com.swiss.wallet.web.dto.ProductResponseDto;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    private final IProductRepository productRepository;
    private final IOrderRepository orderRepository;
    private final IFavoriteRepository favoriteRepository;

    public ProductService(IProductRepository productRepository, IOrderRepository orderRepository, IFavoriteRepository favoriteRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public ProductResponseDto saveProduct(ProductCreateDto createDto, MultipartFile file, int width, int height) {
        Product product = new Product();
        product.setName(createDto.name());
        product.setDescription(createDto.description());
        product.setValue(createDto.value());
        switch (createDto.category()) {
            case "STORE" -> product.setCategory(Category.STORE);
            case "CANTEEN" -> product.setCategory(Category.CANTEEN);
            case "LIBRARY" -> product.setCategory(Category.LIBRARY);
        }
        try {
            String encodedImage = encodeImageToBase64(file, width, height);
            product.setImage(encodedImage);
        } catch (IOException e) {
            throw new RuntimeException("Failed to process image", e);
        }
        product.setAmount(createDto.amount());
        product.setStatus(checkAmount(createDto.amount()));

        productRepository.save(product);
        return ProductResponseDto.toProductResponse(product);
    }

    private StatusProduct checkAmount(Long amount){
        if (amount == 0){
            return StatusProduct.OUT_OF_STOCK;
        }else if (amount <= 10){
            return StatusProduct.LOW_STOCK;
        }
        return StatusProduct.AVAILABLE;
    }

    private String encodeImageToBase64(MultipartFile file, int width, int height) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        BufferedImage resizedImage = Thumbnails.of(originalImage)
                .size(width, height)
                .outputQuality(1.0)  // Maximum quality
                .asBufferedImage();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "png", outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    @Transactional(readOnly = true)
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

    @Transactional
    public void deleteById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException("Product not found, Please check the product ID and try again")
                );

        orderRepository.deleteAllByProduct(product);
        favoriteRepository.deleteAllByProduct(product);
        productRepository.deleteById(product.getId());
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void changeValue(Long id, ChangeProductDto dto) {

        Product product = productRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException("Product not found, Please check the product ID and try again")
                );
        product.setValue(dto.value());
        product.setName(dto.name());
        product.setDescription(dto.description());
        productRepository.save(product);
    }
}
