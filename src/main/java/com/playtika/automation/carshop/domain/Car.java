package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Car {
    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String model;
}
