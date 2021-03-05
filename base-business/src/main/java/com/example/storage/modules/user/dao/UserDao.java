package com.example.storage.modules.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.pojo.vo.SelectUserListPO;
import com.example.storage.modules.user.entity.User;

@Mapper
public interface UserDao extends BaseMapper<User> {

	int add(User user);
	
	User selectUserByMobile(@Param("mobile") String mobile);
	
	User selectUserById(@Param("userId") String userId);

	List<SelectUserListPO> selectValidListByTemplate(Page page, @Param("template") String template, @Param("isDeleted") String isDeleted);

	int selectValidCountByTemplate(@Param("template") String template, @Param("isDeleted") String isDeleted);
	
	int updateUserTypeById(@Param("userType") String userType, @Param("id") String id, @Param("isDeleted") String isDeleted);
	
	List<String> selectIds(Page page, @Param("userType") String userType);
	
	int selectCounts(@Param("userType") String userType);
	
	int updateUserColunms(@Param("mobile") String mobile, @Param("status") String status, @Param("isTestUser") Integer isTestUser, 
			@Param("userType") String userType, @Param("userName") String userName, @Param("id") String id, @Param("isDeleted") String isDeleted);

	
	/**
	 *  任意好友列表
	 */
	List<User> selectReviewListByTemplate(@Param("template") String template, @Param("uid") String uid);


	int selectReviewCountByTemplate(@Param("template") String template, @Param("uid") String keyType);
	
	
	
}
