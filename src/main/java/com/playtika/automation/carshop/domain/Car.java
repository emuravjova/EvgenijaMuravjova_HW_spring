package com.playtika.automation.carshop.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor
public class Car {
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Car plate number", required = true, example = "QW123")
    private String number;
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Car brand", required = true, example = "BMW")
    private String brand;
    @NotNull
    @ApiModelProperty(notes = "Car manufactured year", required = true, example = "2017")
    private Integer year;
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Car color", required = true, example = "green")
    private String color;
}
