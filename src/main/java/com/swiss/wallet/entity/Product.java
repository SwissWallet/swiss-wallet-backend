package com.swiss.wallet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column(name = "value_product")
    private float value;

    private String description;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusProduct status;

    public Product(Long id, String name, float value, String description, String image, Category category, Long amount, StatusProduct status) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.description = description;
        this.image = image;
        this.category = category;
        this.amount = amount;
        this.status = status;
    }

    public Product(String name, float value, String description, String image, Category category, Long amount) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.image = image;
        this.category = category;
        this.amount = amount;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public StatusProduct getStatus() {
        return status;
    }

    public void setStatus(StatusProduct status) {
        this.status = status;
    }
}
