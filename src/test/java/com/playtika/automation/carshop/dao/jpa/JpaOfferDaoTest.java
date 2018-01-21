package com.playtika.automation.carshop.dao.jpa;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.dataset.DataSet;
import com.playtika.automation.carshop.context.JpaRepositoryContext;
import com.playtika.automation.carshop.dao.OfferDao;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by emuravjova on 12/8/2017.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaRepositoryContext.class)
@DataJpaTest
public class JpaOfferDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private JpaOfferDao jpaOfferDao;

    @Test
    @DataSet(value = "offers-table.xml", disableConstraints = true, useSequenceFiltering = false)
    @DBUnit(allowEmptyFields = true)
    public void shouldFindOffersWithOutDeal() {
        List<OfferEntity> offers = jpaOfferDao.findByAcceptedDealIsNull();
        CarEntity car = new CarEntity("AS123", "BMW", 2007, "blue");
        car.setId(1L);
        SellerEntity seller = new SellerEntity("Den", "0501234567");
        seller.setId(1L);
        OfferEntity expectedOffers = new OfferEntity(1L, car, seller, 12000, null);
        assertThat(offers.get(0).getCar(), equalTo(expectedOffers.getCar()));
        assertThat(offers.get(0).getSeller(), equalTo(expectedOffers.getSeller()));
        assertThat(offers.get(0).getAcceptedDeal(), nullValue());
        assertThat(offers.get(0).getId(), is(1L));
        assertThat(offers.size(), is(1));
    }

    @Test
    @DataSet(value = "offers-with-deals-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldReturnEmptyResultIfOffersWithOutDealNotFound() {
        assertTrue(jpaOfferDao.findByAcceptedDealIsNull().isEmpty());
    }

    @Test
    @DataSet(value = "offers-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldFindOfferWithOutDealByCarId() {
        assertThat(jpaOfferDao.findByCarIdAndAcceptedDealIsNull(1L).get(0).getId(), is(equalTo(1L)));
    }

    @Test
    @DataSet(value = "offers-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldReturnEmptyResultIfOfferWithOutDealByCarIdNotFound() {
        assertTrue(jpaOfferDao.findByCarIdAndAcceptedDealIsNull(2L).isEmpty());
    }

    @Test
    @DataSet(value = "offers-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldFindOpenOfferById(){
        assertTrue(jpaOfferDao.findByIdAndAcceptedDealIsNull(1L).isPresent());
    }

    @Test
    @DataSet(value = "offers-with-deals-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldNotFindOpenOfferByIdIfNoOne(){
        assertFalse(jpaOfferDao.findByIdAndAcceptedDealIsNull(1L).isPresent());
    }
}
