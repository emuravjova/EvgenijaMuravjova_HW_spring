package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by emuravjova on 12/6/2017.
 */
public interface OfferDao extends JpaRepository<OfferEntity, Long> {
    List<OfferEntity> findByDeal(DealEntity deal);
    List<OfferEntity> findByCarIdAndDeal(Long id, DealEntity deal);
}

