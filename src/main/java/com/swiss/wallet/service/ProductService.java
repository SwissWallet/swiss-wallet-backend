package com.swiss.wallet.service;

import com.swiss.wallet.repository.IProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }
}
