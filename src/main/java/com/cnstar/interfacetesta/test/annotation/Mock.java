package com.cnstar.interfacetesta.test.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Mocks.class)
public @interface Mock {
    enum MockType {
        GRPC,
        HTTP
    }

    MockType type() default MockType.HTTP;

    String jsonFile();

}
