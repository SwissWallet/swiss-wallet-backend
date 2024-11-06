package com.swiss.wallet.repository;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IBenefitRequestRepository extends JpaRepository<BenefitRequest, Long> {
    List<BenefitRequest> findAllByUser(UserEntity user);
}
