package com.swiss.wallet.service;

import com.swiss.wallet.entity.BenefitActive;
import com.swiss.wallet.entity.BenefitRequest;
import com.swiss.wallet.entity.StatusRequestBenefit;
import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.exception.ObjectNotFoundException;
import com.swiss.wallet.repository.IBenefitActiveRepository;
import com.swiss.wallet.repository.IBenefitRequestRepository;
import com.swiss.wallet.repository.IUserRepository;
import com.swiss.wallet.web.dto.BenefitActiveResponseDto;
import com.swiss.wallet.web.dto.BenefitGlobalResponseDto;
import com.swiss.wallet.web.dto.BenefitReqResponseDto;
import com.swiss.wallet.web.dto.BenefitResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BenefitRequestService {

    private final IBenefitRequestRepository benefitRequestRepository;
    private final IBenefitActiveRepository benefitActiveRepository;
    private final IUserRepository userRepository;

    public BenefitRequestService(IBenefitRequestRepository benefitRequestRepository, IBenefitActiveRepository benefitActiveRepository, IUserRepository userRepository) {
        this.benefitRequestRepository = benefitRequestRepository;
        this.benefitActiveRepository = benefitActiveRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public BenefitRequest createRequestBenefit(Long idUser, Long idBenefit){

        UserEntity user =  userRepository.findById(idUser)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
                );
        BenefitActive benefitActive = benefitActiveRepository.findById(idBenefit)
                .orElseThrow(
                        () -> new ObjectNotFoundException(String.format("Benefit not found. Please check the user ID or username and try again."))
        );
        BenefitRequest benefitRequest = new BenefitRequest();

        benefitRequest.setUser(user);
        benefitRequest.setBenefitActive(benefitActive);
        benefitRequest.setDateTime(LocalDateTime.now());
        benefitRequest.setStatus(StatusRequestBenefit.SENT);
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
            case "SENT" -> request.setStatus(StatusRequestBenefit.SENT);
            case "APPROVED" -> request.setStatus(StatusRequestBenefit.APPROVED);
            case "NOT_APPROVED" -> request.setStatus(StatusRequestBenefit.NOT_APPROVED);
            case "CLOSED" -> request.setStatus(StatusRequestBenefit.CLOSED);
            default -> request.setStatus(StatusRequestBenefit.SENT);
        };

        benefitRequestRepository.save(request);
        removeBenefitRequest(request.getId());
    }

    public void removeBenefitRequest(Long id){
        BenefitRequest request = benefitRequestRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format("Benefit request not found. Please check the user ID or username and try again."))
        );
        benefitRequestRepository.deleteById(request.getId());
    }

    public BenefitGlobalResponseDto getAllByUser(Long id) {

        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException(String.format("User not found. Please check the user ID or username and try again."))
        );

        List<BenefitRequest> benefitRequest = benefitRequestRepository.findAllByUser(user);
        List<Long> ids = new ArrayList<>();


        benefitRequest.stream()
                .forEach(benefitRequest1 -> {
                    ids.add(benefitRequest1.getBenefitActive().getId());
                });

        List<BenefitActive> benefitActives;
        if (!ids.isEmpty()){
            benefitActives = benefitActiveRepository.findByIdNotIn(ids);
        }else {
            benefitActives = benefitActiveRepository.findAll();
        }

        BenefitGlobalResponseDto benefitGlobalResponseDto = new BenefitGlobalResponseDto();
        benefitGlobalResponseDto.setActiveResponseDtos(BenefitActiveResponseDto.toListBenefitrResponse(benefitActives));
        benefitGlobalResponseDto.setReqResponseDtos(BenefitReqResponseDto.toListRequestBenefits(benefitRequest));
        return benefitGlobalResponseDto;
    }
}
