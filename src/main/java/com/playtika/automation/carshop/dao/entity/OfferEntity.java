package com.playtika.automation.carshop.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "offer")
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "car_id", nullable = false)
    private CarEntity car;

    @OneToOne
    @JoinColumn (name = "seller_id", nullable = false)
    private SellerEntity seller;

    @Column(nullable = false)
    @Check(constraints = "price > 0")
    private int price;

    @OneToMany(mappedBy = "offer")
    @Column(name = "deal_id")
    private List<DealEntity> deal;

    public OfferEntity(CarEntity car, SellerEntity seller, int price, List<DealEntity> deal) {
        this.car = car;
        this.seller = seller;
        this.price = price;
        this.deal = deal;
    }
}
