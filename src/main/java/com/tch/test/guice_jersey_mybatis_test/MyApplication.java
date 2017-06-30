package com.tch.test.guice_jersey_mybatis_test;

import com.tch.test.guice_jersey_mybatis_test.interceptor.RestInterceptorBinder;
import org.glassfish.jersey.server.ResourceConfig;

import com.tch.test.guice_jersey_mybatis_test.filter.MyResponseFilter;

/**
 * resource配置
 * 资料参考：https://jersey.java.net/documentation/latest/deployment.html
 */
public class MyApplication extends ResourceConfig {
    public MyApplication() {
    	//所有的resource（类似于mvc里面的controller）包都配置在这里
        packages("com.tch.test.guice_jersey_mybatis_test.resource");
        
        //注册filter
        register(MyResponseFilter.class);

        register(new RestInterceptorBinder());
        //也可以单独注册一个resource
        //register(CommonResource.class);
    }
}