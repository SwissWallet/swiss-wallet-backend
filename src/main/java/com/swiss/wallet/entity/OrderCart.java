package com.swiss.wallet.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orderCarts ")
public class OrderCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private UserEntity user;

    @ManyToMany
    @JoinTable(
            name = "idProduct",
            joinColumns = @JoinColumn(name = "orderCartId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> products;

    @Column(name = "value")
    private float value;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusOrderCart status;

    private LocalDateTime dateTime;

    public OrderCart(Long id, UserEntity user, List<Product> products, float value, StatusOrderCart status) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.value = value;
        this.status = status;
    }

    public OrderCart(UserEntity user, List<Product> products, float value, StatusOrderCart status) {
        this.user = user;
        this.products = products;
        this.value = value;
        this.status = status;
    }

    public OrderCart() {
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public StatusOrderCart getStatus() {
        return status;
    }

    public void setStatus(StatusOrderCart status) {
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
