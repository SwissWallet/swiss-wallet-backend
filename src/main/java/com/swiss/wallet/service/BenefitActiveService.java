package com.swiss.wallet.service;

import com.swiss.wallet.repository.IBenefitActiveRepository;
import org.springframework.stereotype.Service;

@Service
public class BenefitActiveService {

    private final IBenefitActiveRepository benefitActiveRepository;

    public BenefitActiveService(IBenefitActiveRepository benefitActiveRepository) {
        this.benefitActiveRepository = benefitActiveRepository;
    }
}
