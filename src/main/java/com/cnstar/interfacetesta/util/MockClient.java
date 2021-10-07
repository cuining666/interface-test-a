package com.cnstar.interfacetesta.util;

import com.cnstar.interfacetesta.test.annotation.Mock;
import io.restassured.http.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.given;

@Component
public class MockClient {
    private static final String GRPC_CTX_PATH = "/mock-grpc";
    private static final String HTTP_CTX_PATH = "/mock";

    @Value("${mock.grpc.server:http://localhost:28080}")
    private String grpcMockServer;
    @Value("${mock.http.server:http://localhost:8088}")
    private String httpMockServer;

    public void addMock(Mock.MockType mockType, String mockJsonFile) throws IOException {
        String path = getMockMngPath(mockType);
        given().body(new ClassPathResource(mockJsonFile).getInputStream())
                .with().contentType(ContentType.JSON)
                .when().post(new URL(path))
                .then().statusCode(200);
    }

    public void deleteMock(Mock.MockType mockType) {
        String path = getMockMngPath(mockType);
        given().baseUri(httpMockServer + HTTP_CTX_PATH)
                .when().delete(path)
                .then().statusCode(200);
    }

    private String getMockMngPath(Mock.MockType mockType) {
        String path;
        switch (mockType) {
            case GRPC:
                path = grpcMockServer + GRPC_CTX_PATH + "/grpc/stub/general";
                break;
            case HTTP:
                path = httpMockServer + HTTP_CTX_PATH + "/http/stub/general";
                break;
            default:
                path = "";
                break;
        }
        return path;
    }
}
