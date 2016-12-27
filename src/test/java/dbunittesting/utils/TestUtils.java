package dbunittesting.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;

@Component
public class TestUtils {

    private ObjectMapper objectMapper;

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
}
