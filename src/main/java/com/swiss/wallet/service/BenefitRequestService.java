package com.swiss.wallet.service;

import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.Status;
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


    @Transactional(readOnly = true)
    public List<BenefitRequest> getAll(){
        return benefitRequestRepository.findAll();
    }

    @Transactional
    public void updateStatus(Long idBenefit, String status){

        BenefitRequest request = benefitRequestRepository.findById(idBenefit).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Benefit request not found. Please check the user ID or username and try again."))
        );

        switch (status) {
            case "SENT" -> request.setStatus(StatusBenefit.SENT);
            case "UNDER_ANALYSIS" -> request.setStatus(StatusBenefit.UNDER_ANALYSIS);
            case "APPROVED" -> request.setStatus(StatusBenefit.APPROVED);
            case "NOT_APPROVED" -> request.setStatus(StatusBenefit.NOT_APPROVED);
            case "PENDING_DOCUMENTS" -> request.setStatus(StatusBenefit.PENDING_DOCUMENTS);
            case "IN_PROGRESS" -> request.setStatus(StatusBenefit.IN_PROGRESS);
            case "CLOSED" -> request.setStatus(StatusBenefit.CLOSED);
            default -> request.setStatus(StatusBenefit.SENT);
        };

        benefitRequestRepository.save(request);
    }

    public void removeBenefitRequest(Long id){
        BenefitRequest request = benefitRequestRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Benefit request not found. Please check the user ID or username and try again."))
        );

        request.setStatus(StatusBenefit.CLOSED);
        benefitRequestRepository.save(request);

    }

}
