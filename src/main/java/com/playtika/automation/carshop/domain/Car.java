package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Car {
    @NotEmpty
    @NotNull
    private String number;
    @NotEmpty
    @NotNull
    private String brand;
    @NotNull
    private Integer year;
    @NotEmpty
    @NotNull
    private String color;
}
