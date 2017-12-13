package com.playtika.automation.carshop.context;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@Configuration
@EnableCouchbaseRepositories({"com.playtika.automation.carshop.dao.couchbase", "com.playtika.automation.carshop.dao.entity"})
public class CouchbaseRepositoryContext {
}
