package com.tch.test.guice_jersey_mybatis_test.filter;
import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
 
/**
 * filter
 * 资料：https://jersey.java.net/documentation/latest/user-guide.html#filters-and-interceptors
 * @author higgs
 *
 */
public class MyResponseFilter implements ContainerResponseFilter {
 
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {
 
        System.out.println("------filter------");    
    	
    }
    
}