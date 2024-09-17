package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Order;
import com.swiss.wallet.entity.Product;
import com.swiss.wallet.entity.Status;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(UserEntity user);

    Optional<Order> findByIdAndUser(Long idOrder, UserEntity user);

    @Query("SELECT o FROM Order o WHERE o.status= :status")
    List<Order> findAllByStatus(Status status);

    List<Order> findAllByUserAndProductIn(UserEntity user, List<Product> products);

    void deleteAllByUser(UserEntity user);
}
