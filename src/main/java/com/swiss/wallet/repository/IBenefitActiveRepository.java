package com.swiss.wallet.repository;

import com.swiss.wallet.entity.BenefitActive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBenefitActiveRepository extends JpaRepository<BenefitActive, Long> {
}
