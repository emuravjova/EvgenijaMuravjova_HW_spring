package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by emuravjova on 1/19/2018.
 */

public interface JpaCustomerDao extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findFirstByContacts(String contacts);
}
