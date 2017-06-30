package com.tch.test.guice_jersey_mybatis_test.interceptor;

import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import javax.inject.Singleton;

public class RestInterceptorBinder extends AbstractBinder
{
   @Override
   protected void configure()
   {
      bind(RestInterceptorService.class).to(InterceptionService.class).in(Singleton.class);
   }
}