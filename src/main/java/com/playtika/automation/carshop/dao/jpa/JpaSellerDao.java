package com.playtika.automation.carshop.dao.jpa;

import com.playtika.automation.carshop.dao.SellerDao;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * Created by emuravjova on 12/11/2017.
 */
@Repository("jpaSellerDao")
public interface JpaSellerDao extends SellerDao {
}
