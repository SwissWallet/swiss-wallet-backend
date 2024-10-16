package com.swiss.wallet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "idProduct")
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOrder status;

    public Order(Long id, UserEntity user, Product product, StatusOrder status) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.status = status;
    }

    public Order(UserEntity user, Product product, StatusOrder status) {
        this.user = user;
        this.product = product;
        this.status = status;
    }

    public Order() {
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public StatusOrder getStatus() {
        return status;
    }

    public void setStatus(StatusOrder statusOrder) {
        this.status = statusOrder;
    }
}
