package com.playtika.automation.carshop.dao.jpa;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.carshop.context.JpaRepositoryContext;
import com.playtika.automation.carshop.dao.entity.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JpaRepositoryContext.class)
@DataJpaTest
public class JpaDealDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private JpaDealDao jpaDealDao;

    @Test
    @DataSet(value = "deal-table.xml", disableConstraints = true)
    public void shouldFindCarByNumber(){
        List<DealEntity> actualOffer = jpaDealDao.findByOfferId(1L);
        DealEntity expectedDeal = createDeal();
        assertThat(expectedDeal, equalTo(actualOffer));
    }

    @Test
    @DataSet(value = "empty-deal-table.xml", disableConstraints = true)
    @ExpectedDataSet("deal-table.xml")
    public void shouldSaveDeal(){
        DealEntity deal = createDeal();
        jpaDealDao.save(deal);
    }

    private static DealEntity createDeal(){
        CarEntity car = new CarEntity(1L, "AS123", "BMW", 2007, "blue");
        SellerEntity seller = new SellerEntity(1L,"John doe", "0501234567");
        OfferEntity offer = new OfferEntity(car, seller, 20000);
        offer.setId(1L);
        CustomerEntity customer = new CustomerEntity(1L,"Den","1111");
        return new DealEntity(1L, customer, offer, 25000, DealEntity.State.ACTIVE);
    }
}
