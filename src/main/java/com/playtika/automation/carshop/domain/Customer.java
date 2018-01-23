package com.playtika.automation.carshop.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by emuravjova on 1/19/2018.
 */
@Value
@AllArgsConstructor
public class Customer {
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Customer name", required = true, example = "Den")
    private String name;
    @NotEmpty
    @NotNull
    @ApiModelProperty(notes = "Customer contacts", required = true, example = "0967894512")
    private String contacts;
}
