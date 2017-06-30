package com.tch.test.guice_jersey_mybatis_test;

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.palominolabs.http.server.*;
import com.squarespace.jersey2.guice.JerseyGuiceModule;
import com.squarespace.jersey2.guice.JerseyGuiceUtils;
import com.tch.test.guice_jersey_mybatis_test.interceptor.MyResourceInterceptor;
import com.tch.test.guice_jersey_mybatis_test.interceptor.MyServiceInterceptor;
import com.tch.test.guice_jersey_mybatis_test.service.UserService;
import com.tch.test.guice_jersey_mybatis_test.service.UserServiceImpl;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.servlet.ServletContainer;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.builtin.PooledDataSourceProvider;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) throws Exception {
		List<Module> modules = new ArrayList<>();
		//guice-jersey集成部分
		configGuiceJersey(modules);
		
		//guice-mybatis集成部分
		configGuiceMybatis(modules);
		
		//guice-jetty-server集成部分
		configGuiceJettyServer(modules);
		
		//service/repository等业务组件注册
		configBusinessModule(modules);

		//interceptor拦截器module
		configInterceptorModule(modules);

		//依据所有module创建injector
		Injector injector = Guice.createInjector(modules);
		JerseyGuiceUtils.install(injector);
		//jersey server配置和启动
		configAndStartJerseyServer(injector);
		
		//或者使用grizzly server
		//configAndStartGrizzlyServer(injector);
	}
	
	/**
	 * 配置guice-jersey集成
	 * 参考：https://github.com/Squarespace/jersey2-guice/wiki
	 * @param modules
	 */
	private static void configGuiceJersey(List<Module> modules){
		modules.add(new JerseyGuiceModule("__HK2_Generated_0"));
		modules.add(new ServletModule());
	}
	
	/**
	 * service/dao等业务组件绑定
	 * @param modules
	 */
	private static void configBusinessModule(List<Module> modules) {
		modules.add(new AbstractModule() {
		    @Override
		    protected void configure() {
		    	bind(UserService.class).to(UserServiceImpl.class).in(Scopes.SINGLETON);
		    	bind(MyApplication.class);
		    }
		  });
	}

	/**
	 * interceptor拦截器module(https://github.com/mycom-int/jersey-guice-aop)
	 * @param modules
	 */
	private static void configInterceptorModule(List<Module> modules) {
		modules.add(new AbstractModule() {
			@Override
			protected void configure() {
				//参考https://github.com/mycom-int/jersey-guice-aop
				//jersey-guice集成之后,拦截Jersey的resource和其他guice管理对象的拦截是不同的

				//Jersey resource拦截器(在RestInterceptorService中配置)
				MyResourceInterceptor restInterceptor = new MyResourceInterceptor();
				bind(MyResourceInterceptor.class).toInstance(restInterceptor);

				//非resource拦截器
				MyServiceInterceptor normalInterceptor = new MyServiceInterceptor();
				bind(MyServiceInterceptor.class).toInstance(normalInterceptor);

				//非Jersey-resource的拦截器,通过下面的方式配置(只拦截service包下的方法)
				requestInjection(normalInterceptor);
				bindInterceptor(Matchers.inSubpackage("com.tch.test.guice_jersey_mybatis_test.service"),
						Matchers.any(),
						normalInterceptor);
			}
		});
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
	    		myBatisProperties.setProperty("mybatis.environment.id", "demo1");
	    		myBatisProperties.setProperty("JDBC.driver", "com.mysql.jdbc.Driver");
	    		myBatisProperties.setProperty("JDBC.url", "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=UTF-8");
	    		myBatisProperties.setProperty("JDBC.username", "root");
	    		myBatisProperties.setProperty("JDBC.password", "root");
	    		myBatisProperties.setProperty("JDBC.autoCommit", "false");
	    		return myBatisProperties;
	        }
	    });
	}
}
