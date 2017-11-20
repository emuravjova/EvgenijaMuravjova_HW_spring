package com.playtika.automation.carshop.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude={"id"})
public class CarSaleDetails {
    private long id;
    private Car car;
    private SaleInfo saleInfo;
}
