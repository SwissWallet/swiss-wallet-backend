package com.swiss.wallet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "benefits")
public class Benefit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    private float value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusBenefit statusBenefit;

    @ManyToOne
    @JoinColumn(name = "benefitActiveId")
    private BenefitActive benefitActive;

    private LocalDateTime expireDate;

    public Benefit(Long id, UserEntity user, float value, StatusBenefit statusBenefit, BenefitActive benefitActive, LocalDateTime expireDate) {
        this.id = id;
        this.user = user;
        this.value = value;
        this.statusBenefit = statusBenefit;
        this.benefitActive = benefitActive;
        this.expireDate = expireDate;
    }

    public Benefit() {
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

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public StatusBenefit getStatusBenefit() {
        return statusBenefit;
    }

    public void setStatusBenefit(StatusBenefit statusBenefit) {
        this.statusBenefit = statusBenefit;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public BenefitActive getBenefitActive() {
        return benefitActive;
    }

    public void setBenefitActive(BenefitActive benefitActive) {
        this.benefitActive = benefitActive;
    }
}
