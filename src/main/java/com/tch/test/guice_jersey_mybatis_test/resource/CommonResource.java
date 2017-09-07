package com.tch.test.guice_jersey_mybatis_test.resource;

import com.alibaba.fastjson.JSON;
import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.model.Account;
import com.tch.test.guice_jersey_mybatis_test.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("/global")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonResource {

    @Inject
    private AccountService accountService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonResource.class);

    /**
     * 获取系统当前时间
     *
     * @return
     */
    @GET
    @Path("/now")
    public long now() {
        return new Date().getTime();
    }

    @GET
    @Path("/account/{userId}")
    public String getAccount(@PathParam("userId") Long userId) {
        LOGGER.info("userId : {}", userId);
        Account account = accountService.getAccount(userId);
        if (account != null) {
            return account.getChineseName();
        }
        return "用户不存在";
    }

    @POST
    @Path("/account")
    public Account addAccount(Account account) {
        LOGGER.info("userId : {}", JSON.toJSONString(account));
        return accountService.addAccount(account);
    }

}
