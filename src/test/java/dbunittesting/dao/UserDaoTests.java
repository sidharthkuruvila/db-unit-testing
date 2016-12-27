package dbunittesting.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dbunittesting.config.TestConfig;
import dbunittesting.dao.UserDao;
import dbunittesting.dao.UserDaoImpl;
import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.resources.DBFactory;
import dbunittesting.generated.tables.pojos.Users;
import dbunittesting.utils.TestUtils;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;

import static dbunittesting.daofactory.DBType.POSTGRES;

@RunWith(SpringRunner.class)
//@ActiveProfiles("local")
@ContextConfiguration(classes = {TestConfig.class})
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
//        DirtiesContextTestExecutionListener.class,
//        TransactionalTestExecutionListener.class,
//        DbUnitTestExecutionListener.class
//})
////@DatabaseSetup("/dbunittesting/dao/user_tests.xml")
public class UserDaoTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestUtils testUtils;

    @Test
    public void testSomething() throws JsonProcessingException, JSONException {
        UserDao ud = new UserDaoImpl();
        Users u = new Users();
        u.setFirstName("First");
        u.setLastName("Last");
        u.setUsername("user");
        ud.createUser(u, "somewhere");

        Users fu = ud.getUserByUsername("user");
        Assert.assertEquals("First", fu.getFirstName());

        testUtils.assertJsonEquals("fixtures/get_user_by_name_expected.json", fu);

    }
    @After
    public void cleanup() throws Exception {
        testUtils.deleteTable(dbunittesting.generated.tables.Users.USERS);
    }



}
