package com.swiss.wallet.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "adresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String zipCode;

    private String street;

    private String city;

    private Long number;

    private String uf;

    public Address(Long id, String zipCode, String street, String city, Long number, String uf) {
        this.id = id;
        this.zipCode = zipCode;
        this.street = street;
        this.city = city;
        this.number = number;
        this.uf = uf;
    }

    public Address(String zipCode, String street, String city, Long number, String uf) {
        this.zipCode = zipCode;
        this.street = street;
        this.city = city;
        this.number = number;
        this.uf = uf;
    }

    public Address() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
