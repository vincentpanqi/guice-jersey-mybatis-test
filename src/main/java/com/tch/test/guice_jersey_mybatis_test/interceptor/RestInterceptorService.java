package com.tch.test.guice_jersey_mybatis_test.interceptor;

import com.google.common.collect.Lists;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.Path;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class RestInterceptorService implements InterceptionService {
    @Inject
    private Provider<MyInterceptor> interceptorProvider;

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> arg0) {
        return Lists.newArrayList();
    }

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        List<MethodInterceptor> ret = Lists.newArrayList();
        if (method.isAnnotationPresent(Path.class)) {
            ret.add(interceptorProvider.get());
        }
        return ret;
    }

}