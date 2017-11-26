package com.playtika.automation.carshop.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String number;

    @Column(nullable = false, length = 20)
    private String brand;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false, length = 15)
    private String color;

    public CarEntity(String number, String brand, int year, String color) {
        this.number = number;
        this.brand = brand;
        this.year = year;
        this.color = color;
    }
}
