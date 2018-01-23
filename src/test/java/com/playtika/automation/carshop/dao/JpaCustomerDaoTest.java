package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.carshop.dao.entity.CustomerEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;

/**
 * Created by emuravjova on 1/22/2018.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaCustomerDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Autowired
    private JpaCustomerDao jpaCustomerDao;

    @Test
    @DataSet(value = "customer-table.xml", disableConstraints = true)
    public void shouldFindCustomerByContact() {
        CustomerEntity expectedCustomer = new CustomerEntity(1L, "Sam", "0969258649");
        assertThat(jpaCustomerDao.findFirstByContacts("0969258649").get(), samePropertyValuesAs(expectedCustomer));
    }

    @Test
    @DataSet(value = "customer-table.xml", disableConstraints = true)
    public void shouldReturnEmptyResultIfNoCustomerFound() {
        assertThat(jpaCustomerDao.findFirstByContacts("0961111111"), isEmpty());
    }

    @Test
    @DataSet(value = "empty-customer-table.xml", disableConstraints = true)
    @ExpectedDataSet(value = "customer-table.xml", ignoreCols = "id")
    @Commit
    public void shouldSaveCustomer() {
        jpaCustomerDao.save(new CustomerEntity("Sam", "0969258649"));
    }
}
