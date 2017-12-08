package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

/**
 * Created by emuravjova on 12/8/2017.
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class OfferDaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    protected OfferDao offerDao;

    @Test
    @DataSet("offers-table.xml")
    public void shouldFindOffersWithOutDeal(){
        List<OfferEntity> offers = offerDao.findByDealIsNull();
        assertThat(offers.get(0).getId(), is(equalTo(1L)));
        assertThat(offers.size(), is(1));
    }

    @Test
    @DataSet("offers-with-deals-table.xml")
    public void shouldReturnEmptyResultIfOffersWithOutDealNotFound(){
        assertTrue(offerDao.findByDealIsNull().isEmpty());
    }

    @Test
    @DataSet("offers-table.xml")
    public void shouldFindOfferWithOutDealByCarId() {
        assertThat(offerDao.findByCarIdAndDealIsNull(1L).get(0).getId(), is(equalTo(1L)));
    }

    @Test
    @DataSet("offers-table.xml")
    public void shouldReturnEmptyResultIfOfferWithOutDealByCarIdNotFound() {
        assertTrue(offerDao.findByCarIdAndDealIsNull(2L).isEmpty());
    }
}
