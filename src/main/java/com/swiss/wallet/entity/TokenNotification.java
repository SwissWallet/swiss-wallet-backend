package com.swiss.wallet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tokensNotifications")
public class TokenNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne
    @JoinColumn(name = "userId")
    private UserEntity user;

    public TokenNotification(Long id, String token, UserEntity user) {
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public TokenNotification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
