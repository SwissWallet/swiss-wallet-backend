package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.BenefitRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/benefit/actives")
public class BenefitActiveController {

    private final BenefitRequest benefitRequest;

    public BenefitActiveController(BenefitRequest benefitRequest) {
        this.benefitRequest = benefitRequest;
    }
}
