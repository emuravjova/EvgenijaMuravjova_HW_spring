package com.playtika.automation.carshop.web.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by emuravjova on 1/19/2018.
 */
@Data
@AllArgsConstructor
public class DealInfo {
    @ApiModelProperty(notes = "Deal id", required = true, example = "1")
    private long id;
    private long offerId;
}
