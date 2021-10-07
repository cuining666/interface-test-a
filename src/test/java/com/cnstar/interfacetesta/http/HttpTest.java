package com.cnstar.interfacetesta.http;

import com.alibaba.fastjson.JSONObject;
import com.cnstar.interfacetesta.test.BaseTest;
import com.cnstar.interfacetesta.test.annotation.Mock;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@EnableAutoConfiguration
public class HttpTest extends BaseTest {
    private String uuid = UUID.randomUUID().toString();
    private final static String HTTP_TEST_URL = "/http/test";
    @Value("${macing.http.server}")
    private String macingAddr;

    private void setMacingAddr() {
        RestAssured.baseURI = macingAddr;
    }

    @Test
    @Mock(type = Mock.MockType.HTTP, jsonFile = "mock/mock.json")
    public void testHappyPath() {
        setMacingAddr();
        String body = getBody("payload", "01");
        RestAssured.given().
                headers(getHeader(uuid)).body(body).with().contentType(ContentType.JSON).
        when().
                log().all().post(HTTP_TEST_URL).
        then().
                log().all().statusCode(200);
    }

    private String getBody(String payload, String requestType) {
        JSONObject body = new JSONObject();
        body.put("payload", payload);
        body.put("requestType", requestType);
        return body.toJSONString();
    }

    private Map<String, Object> getHeader(String corrId) {
        Map<String, Object> map = new HashMap<>();
        map.put("coor-id", corrId);
        return map;
    }
}
