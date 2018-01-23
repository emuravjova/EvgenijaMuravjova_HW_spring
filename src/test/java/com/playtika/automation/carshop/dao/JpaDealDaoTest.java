package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.carshop.dao.entity.CarEntity;
import com.playtika.automation.carshop.dao.entity.CustomerEntity;
import com.playtika.automation.carshop.dao.entity.DealEntity;
import com.playtika.automation.carshop.dao.entity.OfferEntity;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaDealDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private JpaDealDao jpaDealDao;

    @Test
    @DataSet(value = "empty-deal-table.xml", disableConstraints = true, useSequenceFiltering = false)
    @ExpectedDataSet(value = "deal-table.xml", ignoreCols = "id")
    @Commit
    public void shouldSaveDeal() {
        DealEntity deal = createDeal();
        jpaDealDao.save(deal);
    }

    @Test
    @DataSet(value = "offers-with-deals-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldFindDealByOfferId() {
        List<DealEntity> actualDeals = jpaDealDao.findByOfferId(1L);
        assertThat(actualDeals.get(0).getOffer().getId(), equalTo(1L));
        assertThat(actualDeals.size(), is(1));
    }

    @Test
    @DataSet(value = "offers-with-deals-table.xml", disableConstraints = true, useSequenceFiltering = false)
    public void shouldNotFindDealByOfferIdIfNoOne() {
        assertTrue(jpaDealDao.findByOfferId(3L).isEmpty());
    }

    private static DealEntity createDeal() {
        CarEntity car = new CarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity(1L, "John doe", "0501234567");
        OfferEntity offer = new OfferEntity(1L, car, seller, 20000, null);
        CustomerEntity customer = new CustomerEntity(1L, "Den", "1111");
        return new DealEntity(1L, customer, offer, 25000, DealEntity.State.ACTIVE);
    }
}
