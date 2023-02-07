package com.example.practice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Calendar;

@Entity
@Table(name = "tblproduct")

public class Product {
    @Id
    private Long id;
    private String productName;
    private int year;
    private double price;
    private String url;



    @Transient
    private int age;

    public Product() {
    }

    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }
    public Product(, String productName, int year, double price, String url) {
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getYear() {
        return year;
    }

    public double getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
