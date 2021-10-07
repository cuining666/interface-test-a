package com.cnstar.interfacetesta.test;

import com.cnstar.interfacetesta.test.annotation.Mock;
import com.cnstar.interfacetesta.util.MockClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.Method;

import static io.restassured.RestAssured.given;

@Slf4j
public class BaseTest {
    @Autowired
    private MockClient mockClient;
    @Value("${mock.grpc.server}/mock")
    protected String GRPC_MOCK;
    @Value("${mock.http.server}/mock")
    protected String HTTP_MOCK;

    public void beforeTest(TestInfo testInfo) {
        given().baseUri(HTTP_MOCK).when().delete("/http/stub/general").then().statusCode(200);
    }

    public void setMock(TestInfo testInfo) throws IOException {
        Method method = testInfo.getTestMethod().get();
        if (method == null) {
            return;
        }
        Mock[] mocks = method.getAnnotationsByType(Mock.class);
        if (mocks == null || mocks.length == 0) {
            return;
        }
        for (Mock mock : mocks) {
            mockClient.deleteMock(mock.type());
            mockClient.addMock(mock.type(), mock.jsonFile());
        }
    }
}
