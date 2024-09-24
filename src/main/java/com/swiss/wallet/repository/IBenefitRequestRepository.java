package com.swiss.wallet.repository;

import com.swiss.wallet.entity.BenefitRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBenefitRequestRepository extends JpaRepository<BenefitRequest, Long> {
}
