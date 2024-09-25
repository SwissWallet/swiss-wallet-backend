package com.swiss.wallet.repository;

import com.swiss.wallet.entity.OrderCart;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderCartRepository extends JpaRepository<OrderCart, Long> {
    List<OrderCart> findAllByUser(UserEntity user);

}
