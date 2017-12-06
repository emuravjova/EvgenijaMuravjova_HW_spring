package com.playtika.automation.carshop.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "offer")
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "car_id")
    private CarEntity car;

    @ManyToOne
    @JoinColumn (name = "seller_id")
    private SellerEntity seller;

    private int price;

    @OneToMany(mappedBy = "offer")
    @Column(name = "deal_id")
    private List<DealEntity> deal;

    public OfferEntity(CarEntity car, SellerEntity seller, int price) {
        this.car = car;
        this.seller = seller;
        this.price = price;
    }
}
