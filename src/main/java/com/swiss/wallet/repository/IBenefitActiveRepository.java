package com.swiss.wallet.repository;

import com.swiss.wallet.entity.BenefitActive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IBenefitActiveRepository extends JpaRepository<BenefitActive, Long> {
    boolean existsByTitle(String title);

    List<BenefitActive> findByIdNotIn(List<Long> ids);
}
