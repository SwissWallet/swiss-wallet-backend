package com.swiss.wallet.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "extracts")
public class Extract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Double value;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private Type type;

    private String description;

    @ManyToOne
    @JoinColumn(name = "id_account")
    private Account account;

    private LocalDateTime date;

    public enum Type {
        DEPOSIT, TRANSACTION
    }

    public Extract(Long id, Double value, Type type, String description, Account account, LocalDateTime date) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.description = description;
        this.account = account;
        this.date = date;
    }

    public Extract(Double value, Type type, String description, Account account, LocalDateTime date) {
        this.value = value;
        this.type = type;
        this.description = description;
        this.account = account;
        this.date = date;
    }

    public Extract() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
