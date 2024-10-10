package com.swiss.wallet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "benefitRequests")
public class BenefitRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    private StatusRequestBenefit status;

    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "benefitActiveId")
    private BenefitActive benefitActive;

    public BenefitRequest(Long id, UserEntity user, StatusRequestBenefit status, LocalDateTime dateTime, BenefitActive benefitActive) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.dateTime = dateTime;
        this.benefitActive = benefitActive;
    }

    public BenefitRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public StatusRequestBenefit getStatus() {
        return status;
    }

    public void setStatus(StatusRequestBenefit status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public BenefitActive getBenefitActive() {
        return benefitActive;
    }

    public void setBenefitActive(BenefitActive benefitActive) {
        this.benefitActive = benefitActive;
    }
}
