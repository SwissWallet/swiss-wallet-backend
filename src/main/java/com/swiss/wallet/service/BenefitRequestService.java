package com.swiss.wallet.service;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.StatusBenefit;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IBenefitRequestRepository;
import com.swiss.wallet.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BenefitRequestService {

    private final IBenefitRequestRepository benefitRequestRepository;
    private final IUserRepository userRepository;

    public BenefitRequestService(IBenefitRequestRepository benefitRequestRepository, IUserRepository userRepository) {
        this.benefitRequestRepository = benefitRequestRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BenefitRequest createRequestBenefit(Long idUser, String description){

        UserEntity user =  userRepository.findById(idUser)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        BenefitRequest benefitRequest = new BenefitRequest();

        benefitRequest.setUser(user);
        benefitRequest.setDescription(description);
        benefitRequest.setDateTime(LocalDateTime.now());
        benefitRequest.setStatus(StatusBenefit.SENT);
        return benefitRequestRepository.save(benefitRequest);
    }


    public List<BenefitRequest> getAll(){
        return benefitRequestRepository.findAll();
    }

}
