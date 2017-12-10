package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.couchbase.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "seller")
@Document
public class SellerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    private Long id;

    @Field
    private String name;

    @Field
    private String contacts;

    public SellerEntity(String name, String contacts) {
        this.name = name;
        this.contacts = contacts;
    }
}
