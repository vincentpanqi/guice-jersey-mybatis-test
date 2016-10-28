package com.tch.test.guice_jersey_mybatis_test;

import org.glassfish.jersey.server.ResourceConfig;

public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("com.tch.test.guice_jersey_mybatis_test.resource");
    }
}