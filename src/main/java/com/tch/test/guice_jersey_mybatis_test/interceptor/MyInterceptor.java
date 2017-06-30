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
 * Created by higgs on 2017/6/30.
 */
public class MyInterceptor implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyInterceptor.class);

    @Inject
    private Provider<HttpServletRequest> servletRequestProvider;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        LOGGER.info("MyInterceptor invoked ......");
        if (invocation.getMethod().isAnnotationPresent(Path.class)) {
            Path path = invocation.getMethod().getAnnotation(Path.class);
            long start = System.currentTimeMillis();
            Object ret = invocation.proceed();
            long duration = System.currentTimeMillis() - start;
            HttpServletRequest request = servletRequestProvider.get();
            LOGGER.info("url {}, method {}, duration {}ms", request.getRequestURI(), request.getMethod(), duration);
            return ret;
        }
        return invocation.proceed();
    }

}
