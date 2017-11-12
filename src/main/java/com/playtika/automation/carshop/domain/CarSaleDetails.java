package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CarSaleDetails {
    private long id;
    private Car car;
    private double price;
    private String contacts;
}
