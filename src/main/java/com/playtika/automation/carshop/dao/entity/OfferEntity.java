package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.hibernate.annotations.Check;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Table(name = "offer")
@Document
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    private Long id;

    @ManyToOne
    @JoinColumn (name = "car_id")
    @Field
    private CarEntity car;

    @ManyToOne
    @JoinColumn (name = "seller_id")
    @Field
    private SellerEntity seller;

    private int price;

    @OneToMany(mappedBy = "offer")
    @Column(name = "deal_id")
    @Field
    private List<DealEntity> deal;

    public OfferEntity(CarEntity car, SellerEntity seller, int price) {
        this.car = car;
        this.seller = seller;
        this.price = price;
    }
}
