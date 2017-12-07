package com.playtika.automation.carshop.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "car")
public class CarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String number;

    private String brand;

    private int year;

    private String color;

    public CarEntity(String number, String brand, int year, String color) {
        this.number = number;
        this.brand = brand;
        this.year = year;
        this.color = color;
    }
}
