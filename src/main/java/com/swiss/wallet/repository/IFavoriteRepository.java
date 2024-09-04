package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Favorite;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface IFavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findALlByUser(UserEntity user);

    Favorite findByProductAndUser(Product product, UserEntity user);
}
 