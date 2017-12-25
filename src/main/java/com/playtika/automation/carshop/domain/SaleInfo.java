package com.playtika.automation.carshop.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

/**
 * Created by emuravjova on 11/20/2017.
 */
@Value
@AllArgsConstructor
public class SaleInfo {
    @ApiModelProperty(notes = "Car price", required = true, example = "14000")
    private int price;
    @ApiModelProperty(notes = "Seller contacts", required = true, example = "0985673456")
    private String contacts;
}
