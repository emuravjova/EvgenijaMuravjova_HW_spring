package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface CarDaoCb extends CouchbaseRepository<CarEntity, Long> {
    Optional<CarEntity> findFirstByNumber(String number);
    int deleteById(long id);
}
