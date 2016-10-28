package com.tch.test.guice_jersey_mybatis_test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import com.tch.test.guice_jersey_mybatis_test.mapper.UserMapper;
import com.tch.test.guice_jersey_mybatis_test.service.FooService;
import com.tch.test.guice_jersey_mybatis_test.service.FooServiceMapperImpl;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		List<Module> modules = new ArrayList<>();
		modules.add(new JerseyGuiceModule("__HK2_Generated_0"));
		modules.add(new ServletModule());
		modules.add(new AbstractModule() {
		    @Override
		    protected void configure() {
		    	bind(FooService.class).to(FooServiceMapperImpl.class);
		    	bind(MyApplication.class);
		    }
		  });
		modules.add(new MyBatisModule() {
	        @Override
	        protected void initialize() {
	        	//绑定数据源provider
	            bindDataSourceProviderType(PooledDataSourceProvider.class);
	            //绑定TransactionFactory类型
	            bindTransactionFactoryType(JdbcTransactionFactory.class);
	            //绑定mybatis的mapper相关的类
	            bindMybatisMapper();
	            //绑定DB连接信息
	            Names.bindProperties(binder(), getMybatisProperties());
	        }
	        /**
	         * 绑定mybatis的mapper相关的类
	         */
	        private void bindMybatisMapper() {
	        	//TODO 试一下package scan的方式行不行
	        	//addMapperClass(UserMapper.class);
	        	addMapperClasses("com.tch.test.guice_jersey_mybatis_test.mapper");
			}
	        /**
	         * 获取数据库连接信息
	         * @return
	         */
			private Properties getMybatisProperties(){
	        	Properties myBatisProperties = new Properties();
	    		myBatisProperties.setProperty("mybatis.environment.id", "xxx");
	    		myBatisProperties.setProperty("JDBC.driver", "xxx");
	    		myBatisProperties.setProperty("JDBC.url", "xxx");
	    		myBatisProperties.setProperty("JDBC.username", "xxx");
	    		myBatisProperties.setProperty("JDBC.password", "xxx");
	    		myBatisProperties.setProperty("JDBC.autoCommit", "false");
	    		return myBatisProperties;
	        }
	    });
		Injector injector = Guice.createInjector(modules);
		JerseyGuiceUtils.install(injector);
		// ... continue ...
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
	    HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, injector.getInstance(MyApplication.class));
	    
	    server.start();
	}
}
