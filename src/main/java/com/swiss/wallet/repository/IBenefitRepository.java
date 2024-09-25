package com.swiss.wallet.repository;

import com.swiss.wallet.entity.Benefit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBenefitRepository extends JpaRepository<Benefit, Long> {
}
