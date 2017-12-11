package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.GenerationStrategy;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "deal")
@Document
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    @org.springframework.data.couchbase.core.mapping.id.GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    @Field
    private OfferEntity offer;

    @Field
    private int price;

    @Column(columnDefinition = "ENUM('ACTIVE', 'REJECTED', 'ACCEPTED')", nullable = false)
    @Field
    private State state;

    @Column(name = "actual_date", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    @Field
    private java.sql.Date startDate;

    private enum State {
        ACTIVE, REJECTED, ACCEPTED
    }
}
