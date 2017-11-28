package com.playtika.automation.carshop.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "deal")
public class DealEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private OfferEntity offer;

    @Column(nullable = false)
    @Check(constraints = "price > 0")
    private int price;

    @Column(columnDefinition = "ENUM('ACTIVE', 'REJECTED', 'ACCEPTED')", nullable = false)
    private State state;

    @Column(name = "actual_date", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private java.sql.Date startDate;

    private enum State {
        ACTIVE, REJECTED, ACCEPTED
    }
}
