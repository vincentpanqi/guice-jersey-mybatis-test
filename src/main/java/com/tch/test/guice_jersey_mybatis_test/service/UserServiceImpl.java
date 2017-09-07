package com.tch.test.guice_jersey_mybatis_test.service;

import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.mapper.AccountMapper;
import com.tch.test.guice_jersey_mybatis_test.model.Account;
import org.mybatis.guice.transactional.Transactional;

public class UserServiceImpl implements UserService {

    @Inject
    private AccountMapper accountMapper;

    @Transactional
    public Account getUsername(Long userId) {
        return this.accountMapper.selectByPrimaryKey(userId);
    }

}