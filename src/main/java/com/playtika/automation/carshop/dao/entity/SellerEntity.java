package com.playtika.automation.carshop.dao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "seller")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(unique = true, nullable = false, length = 70)
    private String contacts;

    public SellerEntity(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
    }
}
