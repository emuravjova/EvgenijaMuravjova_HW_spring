package com.playtika.automation.carshop.dao.entity;

import lombok.*;
import org.hibernate.annotations.Check;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
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
