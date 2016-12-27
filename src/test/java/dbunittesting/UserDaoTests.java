package dbunittesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import dbunittesting.dao.UserDao;
import dbunittesting.dao.UserDaoImpl;
import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.resources.DBFactory;
import dbunittesting.generated.tables.pojos.Users;
import dbunittesting.config.TestConfig;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.sql.Connection;

import static dbunittesting.daofactory.DBType.POSTGRES;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("local")
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        //DirtiesContextTestExecutionListener.class,
        //TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
//@DatabaseSetup("/dbunittesting/dao/user_tests.xml")
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
        deleteTable(dbunittesting.generated.tables.Users.USERS);
    }


    private void deleteTable(Table<?> t) throws Exception {
        DBFactory dbFactory = DBProducer.getFactory(POSTGRES);
        try (Connection connection = dbFactory.getConnection()) {
            DSLContext DB = DSL.using(connection, SQLDialect.POSTGRES_9_4);
            DB.delete(t).execute();
        }
    }
}
