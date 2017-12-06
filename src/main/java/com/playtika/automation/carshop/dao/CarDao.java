package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by emuravjova on 12/6/2017.
 */
public interface CarDao extends JpaRepository<CarEntity, Long> {
    List<CarEntity> findByNumber(String number);
    int deleteById(long id);
}
