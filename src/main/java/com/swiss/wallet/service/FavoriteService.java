package com.swiss.wallet.service;

import com.swiss.wallet.entity.Favorite;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.FavoriteAlreadyExistsException;
import com.swiss.wallet.exception.ObjectNotFoundException;
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

    public void saveFavorite(Long id, Long idProduct) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        Product product = productRepository.findById(idProduct)
                .orElseThrow(
                        () -> new ObjectNotFoundException("Product not found, Please check the product ID and try again")
                );

        if(favoriteRepository.findByProductAndUser(product, user) != null){
            throw new FavoriteAlreadyExistsException("Product '" + product.getName() + "' already exists.");
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }
}
