package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.springframework.data.couchbase.repository.CouchbaseRepository;

import java.util.Optional;

public interface SellerDaoCb extends CouchbaseRepository<SellerEntity, Long> {
    Optional<SellerEntity> findFirstByContacts(String contacts);
}