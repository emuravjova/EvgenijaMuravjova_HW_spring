package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
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
import static org.hamcrest.beans.SamePropertyValuesAs.samePropertyValuesAs;

/**
 * Created by emuravjova on 12/8/2017.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class SellerDaoTest {
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    protected SellerDao sellerDao;

    @Test
    @DataSet("seller-table.xml")
    public void shouldFindSellerByContact(){
        Optional<SellerEntity> actualFoundSeller = sellerDao.findFirstByContacts("0969258649");
        SellerEntity expectedSeller = new SellerEntity("Sam", "0969258649");
        expectedSeller.setId(1L);
        assertThat(actualFoundSeller.get(), samePropertyValuesAs(expectedSeller));
    }

    @Test
    @DataSet("seller-table.xml")
    public void shouldReturnEmptyResultIfNoSellerFound(){
        assertThat(sellerDao.findFirstByContacts("0961111111"), isEmpty());
    }
}
