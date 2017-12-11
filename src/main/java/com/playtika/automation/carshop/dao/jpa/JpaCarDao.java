package com.playtika.automation.carshop.dao.jpa;

import com.playtika.automation.carshop.dao.CarDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

/**
 * Created by emuravjova on 12/11/2017.
 */
public interface JpaCarDao extends CarDao {
}
