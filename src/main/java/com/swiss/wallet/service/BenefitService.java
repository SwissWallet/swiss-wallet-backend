package com.swiss.wallet.service;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Benefit;
import com.swiss.wallet.entity.StatusBenefit;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IBenefitRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.BenefitCreateDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Transactional
    public Benefit createBenefit(BenefitCreateDto dto) {
        UserEntity user = userRepository.findById(dto.userId())
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );

        LocalDateTime now = LocalDateTime.now();

        Benefit benefit = new Benefit();
        benefit.setStatusBenefit(StatusBenefit.ACTIVE);
        benefit.setUser(user);
        benefit.setValue(dto.value());
        benefit.setExpireDate(LocalDateTime.now().plusMinutes(dto.months()));
        benefit.setDescription(dto.description() );
        return benefitRepository.save(benefit);

    }
}
