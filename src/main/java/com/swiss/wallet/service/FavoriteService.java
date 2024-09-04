package com.swiss.wallet.service;

import com.swiss.wallet.repository.IFavoriteRepository;
import com.swiss.wallet.repository.IProductRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {

    private final IFavoriteRepository favoriteRepository;
    private final IUserRepository userRepository;
    private final IProductRepository productRepository;

    public FavoriteService(IFavoriteRepository favoriteRepository, IUserRepository userRepository, IProductRepository productRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


}
