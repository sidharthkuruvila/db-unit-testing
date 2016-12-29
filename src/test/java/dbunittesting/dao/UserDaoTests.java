package dbunittesting.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
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
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
//@ActiveProfiles("local")
@ContextConfiguration(classes = {TestConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class
})
////@DatabaseSetup("/dbunittesting/dao/user_tests.xml")
public class UserDaoTests {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TestUtils testUtils;

    @Test
    @Transactional
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
}
