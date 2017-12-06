package com.playtika.automation.carshop.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "seller")
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String contacts;

    public SellerEntity(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
    }
}
