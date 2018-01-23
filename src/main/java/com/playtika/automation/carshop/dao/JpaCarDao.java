package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by emuravjova on 12/6/2017.
 */

public interface JpaCarDao extends JpaRepository<CarEntity, Long> {
    Optional<CarEntity> findFirstByNumber(String number);

    int deleteById(long id);
}
