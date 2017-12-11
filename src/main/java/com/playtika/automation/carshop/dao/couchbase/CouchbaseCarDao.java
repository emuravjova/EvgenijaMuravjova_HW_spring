package com.playtika.automation.carshop.dao.couchbase;

import com.playtika.automation.carshop.dao.CarDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.couchbase.core.query.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by emuravjova on 12/11/2017.
 */

public interface CouchbaseCarDao extends CarDao {

}
