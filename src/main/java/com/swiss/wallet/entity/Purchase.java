package com.swiss.wallet.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "purchases ")
public class Purchase {

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
            joinColumns = @JoinColumn(name = "purchaseId"),
            inverseJoinColumns = @JoinColumn(name = "productId"))
    private List<Product> products;

    @Column(name = "value")
    private float value;

    public Purchase(Long id, UserEntity user, List<Product> products, float value) {
        this.id = id;
        this.user = user;
        this.products = products;
        this.value = value;
    }

    public Purchase(UserEntity user, List<Product> products, float value) {
        this.user = user;
        this.products = products;
        this.value = value;
    }

    public Purchase() {
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
}
