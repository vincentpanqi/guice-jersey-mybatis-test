package com.tch.test.guice_jersey_mybatis_test.filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
 
/**
 * filter
 * 资料：https://jersey.java.net/documentation/latest/user-guide.html#filters-and-interceptors
 * @author higgs
 *
 */
public class MyResponseFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyResponseFilter.class);
 
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
        throws IOException {

        LOGGER.info("filter invoked...");
    	
    }
    
}