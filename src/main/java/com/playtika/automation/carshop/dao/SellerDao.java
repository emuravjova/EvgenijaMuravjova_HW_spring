package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by emuravjova on 12/6/2017.
 */
public interface SellerDao extends JpaRepository<SellerEntity, Long> {
    SellerEntity findFirstByContacts(String contacts);
}
