package com.tch.test.guice_jersey_mybatis_test.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.tch.test.guice_jersey_mybatis_test.vo.School;

public interface DictMapper {

  /**
   * 根据关键字进行模糊查询
   * @param text
   * @return
   */
//	@Results({
//	    @Result(property = "id", column = "id"),
//		@Result(property = "createdAt", column = "created_at"),
//		@Result(property = "updatedAt", column = "updated_at")
//	})
//  @Select("select id, code, title, aliases, grade, created_at, updated_at from school where title" +
//      " like '%${text}%' OR aliases like '%${text}%'")
//	@Select("select id, code, title, aliases, grade, created_at, updated_at from school where title" +
//		      " like CONCAT('%','${text}','%' ) OR aliases like CONCAT('%','${text}','%' )")
//	@Select("select id, code, title, aliases, grade, created_at, updated_at from school where title" +
//		      " like '%'||#{text,jdbcType=VARCHAR}||'%' OR aliases like '%'||#{text,jdbcType=VARCHAR}||'%' ")
	
	  @Results({
	      @Result(property = "createdAt", column = "created_at"),
	      @Result(property = "updatedAt", column = "updated_at")
	  })
	  @Select(" select id, code, title, aliases, grade, created_at, updated_at " +
	      " from school " +
	      " where title like '%'|| #{text} ||'%' " +
	      " OR " +
	      " aliases like '%'|| #{text} ||'%' ")
  List<School> fuzzyByName(String text);
  
}
