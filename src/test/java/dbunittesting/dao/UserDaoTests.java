package dbunittesting.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import dbunittesting.config.TestConfig;
import dbunittesting.generated.tables.pojos.Users;
import dbunittesting.utils.TestUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
//@ActiveProfiles("local")
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup("/fixtures/users_data.xml")
@DbUnitConfiguration(databaseConnection = {"dbUnitDatabaseConnection"})

public class UserDaoTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestUtils testUtils;

    @Test
    public void testFetchUserbyName() throws JsonProcessingException, JSONException {
        UserDao ud = new UserDaoImpl();

        Users fu = ud.getUserByUsername("alice");
        Assert.assertEquals("Alice", fu.getFirstName());

        testUtils.assertJsonEquals("fixtures/fetch_user_by_name_expected_user.json", fu);
    }

    @Test
    @ExpectedDatabase(value = "/fixtures/create_user_expected_db.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    public void testCreateUser() throws Exception {
        UserDao ud = new UserDaoImpl();
        Users u = new Users();
        u.setFirstName("First");
        u.setLastName("Last");
        u.setUsername("user");
        ud.createUser(u, "somewhere");
        Assert.assertNotNull(u.getId());
        //testUtils.generateExpectedDbUtilsFile("src/test/resources/fixtures/create_user_expected_db.xml", Lists.newArrayList("users"));
    }


}
