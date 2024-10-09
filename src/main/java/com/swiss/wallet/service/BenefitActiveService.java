package com.swiss.wallet.service;

import com.swiss.wallet.entity.BenefitActive;
import com.swiss.wallet.exception.UserUniqueViolationException;
import com.swiss.wallet.repository.IBenefitActiveRepository;
import com.swiss.wallet.web.dto.BenefitActiveCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BenefitActiveService {

    private final IBenefitActiveRepository benefitActiveRepository;

    public BenefitActiveService(IBenefitActiveRepository benefitActiveRepository) {
        this.benefitActiveRepository = benefitActiveRepository;
    }

    @Transactional
    public BenefitActive createBenefit(BenefitActiveCreateDto dto){
        BenefitActive benefitActive = new BenefitActive();
        benefitActive.setTitle(dto.title());
        benefitActive.setDescription(dto.description());
        if (benefitActiveRepository.existsByTitle(dto.title())){
            throw new UserUniqueViolationException("Benefit already registered");
        }
        return benefitActiveRepository.save(benefitActive);
    }
}
