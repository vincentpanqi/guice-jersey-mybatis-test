package com.tch.test.guice_jersey_mybatis_test.service;

import com.tch.test.guice_jersey_mybatis_test.model.Account;

public interface AccountService {

	Account getAccount(Long userId);

	Account addAccount(Account account);

}
