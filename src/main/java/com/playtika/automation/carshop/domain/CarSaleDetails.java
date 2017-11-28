package com.playtika.automation.carshop.domain;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude={"carId"})
public class CarSaleDetails {
    private long carId;
    private Car car;
    private SaleInfo saleInfo;
}
