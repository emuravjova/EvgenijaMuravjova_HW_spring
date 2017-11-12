package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Car {
    @NotNull
    private String name;
    @NotNull
    private String model;
}
