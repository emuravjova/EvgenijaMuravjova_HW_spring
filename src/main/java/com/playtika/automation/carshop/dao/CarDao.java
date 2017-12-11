package com.playtika.automation.carshop.dao;

import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.Id;
import java.util.List;
import java.util.Optional;

/**
 * Created by emuravjova on 12/6/2017.
 */

@NoRepositoryBean
public interface CarDao extends CrudRepository<CarEntity, Long> {
    Optional<CarEntity> findFirstByNumber(String number);
    int deleteById(long id);
}
