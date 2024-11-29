package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Benefit;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBenefitRepository extends JpaRepository<Benefit, Long> {
    List<Benefit> findAllByUser(UserEntity user);
}
