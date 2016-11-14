package com.tch.test.guice_jersey_mybatis_test.resource;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.mapper.DictMapper;
import com.tch.test.guice_jersey_mybatis_test.mapper.UserMapper;
import com.tch.test.guice_jersey_mybatis_test.service.UserService;
import com.tch.test.guice_jersey_mybatis_test.vo.MyVo;
import com.tch.test.guice_jersey_mybatis_test.vo.School;

@Path("/global")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonResource {

	@Inject
	private UserService userService;
	
	@Inject
	private DictMapper dictMapper;
	
	@Inject
	private UserMapper userMapper;
	
  /**
   * 获取系统当前时间
   * @return
   */
  @GET
  @Path("/now")
  public long now() {
    return new Date().getTime();
  }
  
  @GET
  @Path("/getUsername/{userId}")
  public String getUsername(@PathParam("userId") String userId) {
	System.out.println("userId : " + userId);
    String username;
	try {
		username = userService.getUsername(userId);
		return username;
	} catch (Exception e) {
		e.printStackTrace();
		return e.getMessage();
	}
  }
  
  @GET
  @Path("/fuzzyByName")
  public String fuzzyByName(@QueryParam("text") String text) {
    List<School> schools;
	try {
		System.out.println("text: " + text);
		schools = dictMapper.fuzzyByName(text);
//		System.out.println(schools);
		return schools.toString();
	} catch (Exception e) {
		e.printStackTrace();
		return e.getMessage();
	}
  }
  
  @GET
  @Path("/mybatis/test")
  public List<MyVo> testMybatisAnnotation(){
	  return userMapper.getExistOrgNames(Arrays.asList(1, 2, 3));
  }

}
