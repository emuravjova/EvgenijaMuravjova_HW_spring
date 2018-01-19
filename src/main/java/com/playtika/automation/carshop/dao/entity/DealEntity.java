package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Entity;
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
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    @org.springframework.data.couchbase.core.mapping.id.GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn (name = "customer_id")
    @Field
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    @Field
    private OfferEntity offer;

    @Field
    private int price;

    @Column(columnDefinition = "ENUM('ACTIVE', 'REJECTED', 'ACCEPTED')")
    @Field
    private State state;

    public enum State {
        ACTIVE, REJECTED, ACCEPTED
    }

    public DealEntity(CustomerEntity customer, OfferEntity offer, int price, State state) {
        this.customer = customer;
        this.offer = offer;
        this.price = price;
        this.state = state;
    }
}
