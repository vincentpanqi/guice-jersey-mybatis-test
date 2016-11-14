package com.tch.test.guice_jersey_mybatis_test.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.tch.test.guice_jersey_mybatis_test.vo.MyVo;

public interface UserMapper {

    @Select("SELECT username FROM account WHERE id = #{userId}")
    String getUsername(@Param("userId") String userId);

    @Select({"<script>",
        "select id, code, title ",
        "FROM dict ",
        "WHERE id IN",
        "<foreach item='item' index='index' collection='ids'",
        "open='(' separator=',' close=')'>",
        "#{item}",
        "</foreach>",
        "</script>"})
    @Results
    List<MyVo> getExistOrgNames(@Param("ids") List<Integer> ids);
    
}