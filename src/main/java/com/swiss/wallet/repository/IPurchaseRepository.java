package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.Purchase;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findAllByUser(UserEntity user);

}
