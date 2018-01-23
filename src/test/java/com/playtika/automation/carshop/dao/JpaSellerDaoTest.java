package com.playtika.automation.carshop.dao;

import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.playtika.automation.carshop.dao.entity.SellerEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isEmpty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

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
    @DataSet(value = "empty-seller-table.xml", disableConstraints = true)
    @ExpectedDataSet(value = "seller-table.xml", ignoreCols = "id")
    @Commit
    public void shouldSaveSeller() {
        jpaSellerDao.save(new SellerEntity(1L, "Sam", "0969258649"));
    }

    @Test
    @DataSet(value = "seller-table.xml", disableConstraints = true)
    public void shouldFindSellerByContact() {
        SellerEntity expectedSeller = new SellerEntity(1L, "Sam", "0969258649");
        Optional<SellerEntity> actualFoundSeller = jpaSellerDao.findFirstByContacts("0969258649");
        assertThat(actualFoundSeller.get(), equalTo(expectedSeller));
    }

    @Test
    @DataSet(value = "seller-table.xml", disableConstraints = true)
    public void shouldReturnEmptyResultIfNoSellerFound() {
        assertThat(jpaSellerDao.findFirstByContacts("0961111111"), isEmpty());
    }

}
