package com.playtika.automation.carshop.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarId {
    @ApiModelProperty(notes = "Car id", required = true, example = "1")
    private long id;
}
