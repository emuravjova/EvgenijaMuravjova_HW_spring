package com.playtika.automation.carshop.dao.jpa;

import com.playtika.automation.carshop.dao.OfferDao;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * Created by emuravjova on 12/11/2017.
 */
@Repository("jpaOfferDao")
public interface JpaOfferDao extends OfferDao {
}
