package com.tch.test.guice_jersey_mybatis_test.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("SELECT username FROM account WHERE id = #{userId}")
    String getUsername(@Param("userId") String userId);

}