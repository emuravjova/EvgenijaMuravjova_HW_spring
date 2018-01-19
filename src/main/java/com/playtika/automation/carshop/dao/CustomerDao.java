package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CustomerEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Created by emuravjova on 1/19/2018.
 */
@NoRepositoryBean
public interface CustomerDao extends CrudRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findFirstByContacts(String contacts);
}
