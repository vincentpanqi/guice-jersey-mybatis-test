package com.tch.test.guice_jersey_mybatis_test.service;

import com.google.inject.Inject;
import com.tch.test.guice_jersey_mybatis_test.mapper.AccountMapper;
import com.tch.test.guice_jersey_mybatis_test.model.Account;
import org.mybatis.guice.transactional.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl() {
        LOGGER.info("AccountServiceImpl created");
    }

    @Inject
    private AccountMapper accountMapper;

    @Transactional
    public Account getAccount(Long userId) {
        return this.accountMapper.selectByPrimaryKey(userId);
    }

    @Override
    public Account addAccount(Account account) {
        accountMapper.insertSelective(account);
        return account;
    }
}