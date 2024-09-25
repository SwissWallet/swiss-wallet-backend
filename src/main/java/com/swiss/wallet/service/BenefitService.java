package com.swiss.wallet.service;

import com.swiss.wallet.entity.*;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IAccountRepository;
import com.swiss.wallet.repository.IBenefitRepository;
import com.swiss.wallet.repository.IExtractRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.BenefitCreateDto;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BenefitService {

    private final IBenefitRepository benefitRepository;
    private final IUserRepository userRepository;
    private final IAccountRepository accountRepository;
    private final IExtractRepository extractRepository;

    private static final Logger logger = LoggerFactory.getLogger(BenefitService.class);

    public BenefitService(IBenefitRepository benefitRepository, IUserRepository userRepository, IAccountRepository accountRepository, IExtractRepository extractRepository) {
        this.benefitRepository = benefitRepository;
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.extractRepository = extractRepository;
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

    @Transactional
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void depositBenefit(){
        List<Benefit> benefits = benefitRepository.findAll();
        benefits.stream()
                .forEach(benefit -> {
                    if (!benefit.getExpireDate().isAfter(LocalDateTime.now())){
                        benefit.setStatusBenefit(StatusBenefit.INACTIVE);
                        benefitRepository.save(benefit);
                    }else if(benefit.getStatusBenefit() == StatusBenefit.INACTIVE) {
                        logger.info("Inactive benefit");
                    }else{
                            Account account = accountRepository.findAccountByUser(benefit.getUser()).orElseThrow(
                                    () -> new ObjectNotFoundException(String.format("Account id = %s not found", benefit.getUser().getName()))
                            );
                            account.setValue(account.getValue() + benefit.getValue());

                            Extract extract = new Extract();
                            extract.setAccount(account);
                            extract.setValue((double) benefit.getValue());
                            extract.setType(Extract.Type.DEPOSIT);
                            extract.setDescription("Benefit deposit");
                            extract.setDate(LocalDateTime.now());
                            extractRepository.save(extract);
                    }

                });
        LocalDateTime dateTime = LocalDateTime.now();
        logger.info("Running at {}", dateTime);
    }
}
