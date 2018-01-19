package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Created by emuravjova on 1/19/2018.
 */
@NoRepositoryBean
public interface DealDao extends CrudRepository<DealEntity, Long> {
}
