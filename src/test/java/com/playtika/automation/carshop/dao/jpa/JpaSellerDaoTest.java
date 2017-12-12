package com.playtika.automation.carshop.dao.jpa;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.carshop.dao.SellerDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

/**
 * Created by emuravjova on 12/8/2017.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaSellerDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private JpaSellerDao jpaSellerDao;

    @Test
    @DataSet(value = "seller-table.xml", disableConstraints = true)
    public void shouldFindSellerByContact(){
        Optional<SellerEntity> actualFoundSeller = jpaSellerDao.findFirstByContacts("0969258649");
        SellerEntity expectedSeller = new SellerEntity("Sam", "0969258649");
        expectedSeller.setId(1L);
        assertThat(actualFoundSeller.get(), equalTo(expectedSeller));
    }

    @Test
    @DataSet(value = "seller-table.xml", disableConstraints = true)
    public void shouldReturnEmptyResultIfNoSellerFound(){
        assertThat(jpaSellerDao.findFirstByContacts("0961111111"), isEmpty());
    }
}
