package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Created by emuravjova on 12/6/2017.
 */

@NoRepositoryBean
public interface SellerDao extends CrudRepository<SellerEntity, Long> {
    Optional<SellerEntity> findFirstByContacts(String contacts);
}
