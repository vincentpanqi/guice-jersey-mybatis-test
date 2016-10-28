package com.tch.test.guice_jersey_mybatis_test.service;

import org.mybatis.guice.transactional.Transactional;

import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.mapper.UserMapper;

public class FooServiceMapperImpl implements FooService {

    @Inject
    private UserMapper userMapper;

    @Transactional
    public String getUsername(String userId) {
        return this.userMapper.getUsername(userId);
    }

}