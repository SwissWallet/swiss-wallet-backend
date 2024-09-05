package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Category;
import com.swiss.wallet.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where name like %:name%")
    List<Product> findAllByName(String name);

    @Query("select p from Product p where p.category = :category")
    List<Product> findAllByCategory(@Param("category") Category category);
}
