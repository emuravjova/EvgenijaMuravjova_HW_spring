package com.playtika.automation.carshop.dao.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@Table(name = "deal")
@Document
@NoArgsConstructor
@AllArgsConstructor
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(targetEntity = CustomerEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", columnDefinition = "BIGINT")
    private CustomerEntity customer;

    @ManyToOne(targetEntity = OfferEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id", columnDefinition = "BIGINT")
    private OfferEntity offer;

    @Column(nullable = false)
    private int price;

    public enum State {
        ACTIVE, REJECTED, ACCEPTED
    }

    @Column(columnDefinition = "ENUM('ACTIVE', 'REJECTED', 'ACCEPTED')")
    private State state;

    public DealEntity(CustomerEntity customer, OfferEntity offer, int price, State state) {
        this.customer = customer;
        this.offer = offer;
        this.price = price;
        this.state = state;
    }
}
