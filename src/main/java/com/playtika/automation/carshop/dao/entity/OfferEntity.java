package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.hibernate.annotations.Check;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "offer")
@Document
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    @org.springframework.data.couchbase.core.mapping.id.GeneratedValue
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

    @OneToOne
    @Field
    private DealEntity acceptedDeal;

    public OfferEntity(CarEntity car, SellerEntity seller, int price) {
        this.car = car;
        this.seller = seller;
        this.price = price;
    }
}
