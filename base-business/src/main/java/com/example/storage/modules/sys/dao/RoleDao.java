package com.example.storage.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.entity.Role;
import com.example.storage.modules.sys.pojo.vo.back.RoleVo;

@Mapper
public interface RoleDao  extends BaseMapper<Role>{
	List<Role> getRoleByUserId(String id);

	Integer addRole(@Param("user_id") String uid, @Param("role_id") String roleId);

	Role selectRoleByRoleName(String roleName);

	Integer deleteRole(@Param("user_id") String userId, @Param("role_id") String roleId);

	Role selectRoleById(@Param("user_id") String userId, @Param("role_id") String roleId);

	void updateRoleById(@Param("user_id") String userId, @Param("role_id") String roleId);

	List<RoleVo> getAllRoles();

	Integer newRole(@Param("id") String uuid, @Param("role_name") String roleName, @Param("ch_name") String chName);

	Integer selsectRoleCount(@Param("user_id") String uid);

	//
	



    Role selectRoleByRoleName(@Param("roleName") String roleName, @Param("isDeleted") String isDeleted);

    
	List<Role> selectValidList(@Param("page") Page page, @Param("isDeleted") String isDeleted);

	List<Role> selectValidListByTemplate(Page<Role> page, @Param("template") String template,
			@Param("isDeleted") String isDeleted);

	int selectValidCount(@Param("isDeleted") String isDeleted);

	int selectValidCountByTemplate(@Param("template") String template, @Param("isDeleted") String isDeleted);

	int updateStatus(@Param("isDeleted") String isDeleted, @Param("id") String id);
	
	   //查询所有角色名
    List<String> getAllRoleNames(@Param("isDeleted") String isDeleted);
}
