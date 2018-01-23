package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.DealEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by emuravjova on 1/19/2018.
 */

public interface JpaDealDao extends JpaRepository<DealEntity, Long> {
    List<DealEntity> findByOfferId(Long id);
}
