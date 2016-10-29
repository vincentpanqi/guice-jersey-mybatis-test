package com.tch.test.guice_jersey_mybatis_test;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * resource配置
 * 资料参考：https://jersey.java.net/documentation/latest/deployment.html
 */
public class MyApplication extends ResourceConfig {
    public MyApplication() {
    	//所有的resource（类似于mvc里面的controller）包都配置在这里
        packages("com.tch.test.guice_jersey_mybatis_test.resource");
        
        //也可以单独注册一个resource
        //register(CommonResource.class);
    }
}