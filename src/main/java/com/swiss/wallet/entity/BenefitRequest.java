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

    private StatusBenefit status;

    private LocalDateTime dateTime;

    private String description;

    public BenefitRequest(StatusBenefit status, LocalDateTime dateTime, String description) {
        this.status = status;
        this.dateTime = dateTime;
        this.description = description;
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

    public StatusBenefit getStatus() {
        return status;
    }

    public void setStatus(StatusBenefit status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
