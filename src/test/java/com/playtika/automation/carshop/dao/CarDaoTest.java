package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;

/**
 * Created by emuravjova on 12/8/2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CarDaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private CarDao carDao;

    @Test
    @DataSet("car-table.xml")
    public void shouldFindCarByNumber(){
        Optional<CarEntity> actualFoundCar = carDao.findFirstByNumber("AS123");
        CarEntity expectedCar = new CarEntity("AS123","BMW", 2007, "blue");
        expectedCar.setId(1L);
        assertThat(actualFoundCar.get(), equalTo(expectedCar));
    }

    @Test
    @DataSet("car-table.xml")
    public void shouldReturnEmptyResultIfNoCarFound(){
        assertThat(carDao.findFirstByNumber("ADD123"), isEmpty());
    }

    @Test
    @DataSet("car-table.xml")
    @ExpectedDataSet("empty-car-table.xml")
    @Commit
    public void shouldDeleteCarById(){
        int countOfDeletedCar = carDao.deleteById(1L);
        assertThat(countOfDeletedCar, is(1));
    }
}
