package com.swiss.wallet.web.controller;

import com.swiss.wallet.service.BenefitRequestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/benefit-requests")
public class BenefirRequestController {

    private final BenefitRequestService benefitRequestService;

    public BenefirRequestController(BenefitRequestService benefitRequestService) {
        this.benefitRequestService = benefitRequestService;
    }
}
