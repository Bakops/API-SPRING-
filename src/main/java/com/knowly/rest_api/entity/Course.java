package com.knowly.rest_api.entity;

import java.io.Serializable;

public class Course implements Serializable {
    private Long id;
    private String name;
    private Double price;

    public Course() {
    }

    public Course(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Course(String name, Double price) {
        this.name = name;
        this.price = price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
