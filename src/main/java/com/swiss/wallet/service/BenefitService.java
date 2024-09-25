package com.swiss.wallet.service;

import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IBenefitRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class BenefitService {

    private final IBenefitRepository benefitRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;

    public BenefitService(IBenefitRepository benefitRepository, IUserRepository userRepository, IAccountRepository accountRepository) {
        this.benefitRepository = benefitRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }
}
