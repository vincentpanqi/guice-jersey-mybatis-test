package com.tch.test.guice_jersey_mybatis_test.resource;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.service.UserService;

@Path("/global")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonResource {

	@Inject
	private UserService userService;
	
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

}
