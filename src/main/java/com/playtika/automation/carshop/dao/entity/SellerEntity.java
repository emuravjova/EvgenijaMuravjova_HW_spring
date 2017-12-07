package com.playtika.automation.carshop.dao.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
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
