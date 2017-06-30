package com.tch.test.guice_jersey_mybatis_test.interceptor;

import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * 拦截除了Jersey-resource之外的method拦截器
 * Created by higgs on 2017/6/30.
 */
public class MyServiceInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServiceInterceptor.class);

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        LOGGER.info("service拦截器执行,method {}", invocation.getMethod().getName());
        return invocation.proceed();
    }

}
