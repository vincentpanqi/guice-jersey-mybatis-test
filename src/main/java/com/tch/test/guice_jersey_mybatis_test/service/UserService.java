package com.tch.test.guice_jersey_mybatis_test.service;

import com.tch.test.guice_jersey_mybatis_test.model.Account;

public interface UserService {

	Account getUsername(Long userId);
	
}
