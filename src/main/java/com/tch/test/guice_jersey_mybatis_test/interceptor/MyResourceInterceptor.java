package com.tch.test.guice_jersey_mybatis_test.interceptor;

import com.google.inject.Provider;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

/**
 * 拦截Jersey-resource的method拦截器
 * Created by higgs on 2017/6/30.
 */
public class MyResourceInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyResourceInterceptor.class);

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        if (invocation.getMethod().isAnnotationPresent(Path.class)) {
            Path path = invocation.getMethod().getAnnotation(Path.class);
            HttpServletRequest request = servletRequestProvider.get();
            LOGGER.info("jersey-resource拦截器执行,url {}, method {}", request.getRequestURI(), request.getMethod());
            Object ret = invocation.proceed();
            return ret;
        }
        return invocation.proceed();
    }

}
