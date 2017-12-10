package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.List;

public interface OfferDaoCb extends CouchbaseRepository<OfferEntity, Long> {
    List<OfferEntity> findByDealIsNull();
    List<OfferEntity> findByCarIdAndDealIsNull(Long id);
}
