package com.playtika.automation.carshop.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "offer")
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(targetEntity = CarEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false, columnDefinition = "BIGINT")
    private CarEntity car;

    @ManyToOne(targetEntity = SellerEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false, columnDefinition = "BIGINT")
    private SellerEntity seller;

    @Column(nullable = false)
    @Check(constraints = "price > 0")
    private int price;

    @OneToOne
    private DealEntity acceptedDeal;

    public OfferEntity(CarEntity car, SellerEntity seller, int price) {
        this.car = car;
        this.seller = seller;
        this.price = price;
    }
}
