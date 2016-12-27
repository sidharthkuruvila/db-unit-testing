package dbunittesting.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import dbunittesting.daofactory.DBProducer;
import dbunittesting.daofactory.resources.DBFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Connection;

import static dbunittesting.daofactory.DBType.POSTGRES;

@Component
public class TestUtils {

    private ObjectMapper objectMapper;

    @Autowired
    public TestUtils(ObjectMapper objectMapper) {

        this.objectMapper = objectMapper;
    }

    public String fixture(String path) {
        try {
            return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void assertJsonEquals(String fixturePath, Object object) {
        String objectJson = null;
        try {
            objectJson = objectMapper.writeValueAsString(object);
            String fixtureJson = fixture(fixturePath);
            JSONAssert.assertEquals(fixtureJson, objectJson, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTable(Table<?> t) throws Exception {
        DBFactory dbFactory = DBProducer.getFactory(POSTGRES);
        try (Connection connection = dbFactory.getConnection()) {
            DSLContext DB = DSL.using(connection, SQLDialect.POSTGRES_9_4);
            DB.delete(t).execute();
        }
    }

    public <T> T readJsonFixture(String fixturePath, TypeReference<T> typeReference){
        String fixtureJson = fixture(fixturePath);
        try {
            return objectMapper.readValue(fixtureJson, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
