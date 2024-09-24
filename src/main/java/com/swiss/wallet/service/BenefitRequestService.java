package com.swiss.wallet.service;

import com.swiss.wallet.repository.IBenefitRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class BenefitRequestService {

    private final IBenefitRequestRepository benefitRequestRepository;

    public BenefitRequestService(IBenefitRequestRepository benefitRequestRepository) {
        this.benefitRequestRepository = benefitRequestRepository;
    }
}
