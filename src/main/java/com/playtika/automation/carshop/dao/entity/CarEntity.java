package com.playtika.automation.carshop.dao.entity;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.*;
import org.springframework.data.annotation.*;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.id.*;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Document
@Table(name = "car")
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @com.couchbase.client.java.repository.annotation.Id
    @org.springframework.data.couchbase.core.mapping.id.GeneratedValue
    private Long id;

    @Field
    private String number;
    @Field
    private String brand;
    @Field
    private int year;
    @Field
    private String color;

    public CarEntity(String number, String brand, int year, String color) {
        this.number = number;
        this.brand = brand;
        this.year = year;
        this.color = color;
    }

}
