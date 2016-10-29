package com.tch.test.guice_jersey_mybatis_test;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.UriBuilder;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.palominolabs.http.server.HttpResourceHandlerConfig;
import com.palominolabs.http.server.HttpServerConnectorConfig;
import com.palominolabs.http.server.HttpServerWrapperConfig;
import com.palominolabs.http.server.HttpServerWrapperFactory;
import com.palominolabs.http.server.HttpServerWrapperModule;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import com.tch.test.guice_jersey_mybatis_test.service.UserService;
import com.tch.test.guice_jersey_mybatis_test.service.UserServiceImpl;

/**
 * Hello world!
 *
 */
public class App {
	
	/**
	 * 配置guice-jersey集成
	 * 参考：https://github.com/Squarespace/jersey2-guice/wiki
	 * @param modules
	 */
	private static void configGuiceJersey(List<Module> modules){
		modules.add(new JerseyGuiceModule("__HK2_Generated_0"));
		modules.add(new ServletModule());
		modules.add(new AbstractModule() {
		    @Override
		    protected void configure() {
		    	//注册其他组件
		    	bind(UserService.class).to(UserServiceImpl.class);
		    	//注册resource管理组件
		    	bind(MyApplication.class);
		    }
		  });
	}
	public static void main(String[] args) throws Exception {
		List<Module> modules = new ArrayList<>();
		//guice-jersey集成部分
		configGuiceJersey(modules);
		
		//guice-mybatis集成部分
		configGuiceMybatis(modules);
		
		//guice-jetty-server集成部分
		configGuiceJettyServer(modules);
		
		//依据所有module创建injector
		Injector injector = Guice.createInjector(modules);
		JerseyGuiceUtils.install(injector);
		//jersey server配置和启动
		configAndStartJerseyServer(injector);
		
		//或者使用grizzly server
		//configAndStartGrizzlyServer(injector);
	}
	
	public static void configAndStartGrizzlyServer(Injector injector) throws IOException {
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(8080).build();
	    HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, injector.getInstance(MyApplication.class));
	    server.start();
	}
	
	/**
	 * 参考：https://github.com/palominolabs/jetty-http-server-wrapper
	 * @param injector
	 * @throws Exception
	 */
	public static void configAndStartJerseyServer(Injector injector) throws Exception{
		//静态资源访问配置
		HttpResourceHandlerConfig rhConfig = new HttpResourceHandlerConfig()
			    .withBaseResource(Resource.newClassPathResource(""))
			    .withContextPath("/static");
		//host和端口配置
		HttpServerWrapperConfig config = new HttpServerWrapperConfig()
		            .withResourceHandlerConfig(rhConfig)
		            .withHttpServerConnectorConfig(HttpServerConnectorConfig.forHttp("localhost", 8080));
		//应用上面的配置并启动
		injector.getInstance(HttpServerWrapperFactory.class)
		    .getHttpServerWrapper(config)
		    .start();
	}
	
	/**
	 * 配置guice-jetty-server
	 * 参考：https://github.com/palominolabs/jetty-http-server-wrapper
	 * @param modules
	 */
	private static void configGuiceJettyServer(List<Module> modules) {
		modules.add(new AbstractModule() {
	        @Override
	        protected void configure() {
	            // module for this library
	            install(new HttpServerWrapperModule());
	            // servlet module to bind servlets to paths
	            install(new ServletModule() {
	                @Override
	                protected void configureServlets() {
	                    bind(ServletContainer.class).toProvider(new Provider<ServletContainer>() {
	                    	@Inject
	                    	private MyApplication resourceConfig;
							@Override
							public ServletContainer get() {
								return new ServletContainer(resourceConfig);
							}
						}).in(Scopes.SINGLETON);;
	                    serve("/*").with(ServletContainer.class);
	                }
	            });
	        }
	    });
		
	}
	
	private static void configGuiceMybatis(List<Module> modules) {
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
	        	//将制定package下面的类都注册为mapper
	        	addMapperClasses("com.tch.test.guice_jersey_mybatis_test.mapper");
	        	//也可以单独注册某一个mapper
	        	//addMapperClass(UserMapper.class);
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
	}
}
