package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Created by emuravjova on 12/6/2017.
 */

@NoRepositoryBean
public interface OfferDao extends CrudRepository<OfferEntity, Long> {
    List<OfferEntity> findByAcceptedDealIsNull();
    List<OfferEntity> findByCarIdAndAcceptedDealIsNull(Long id);
    Optional<OfferEntity> findByIdAndAcceptedDealIsNull(Long id);
    Optional<OfferEntity> findByDealsIdAndAcceptedDealIsNull(Long id);
}

