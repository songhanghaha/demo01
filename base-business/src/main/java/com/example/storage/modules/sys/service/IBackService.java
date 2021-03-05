package com.example.storage.modules.sys.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.Role;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.vo.AddManagerUserDTO;
import com.example.storage.modules.sys.pojo.vo.AddRoleDto;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.pojo.vo.SelectManagerUserInfo;
import com.example.storage.modules.sys.pojo.vo.back.UserInfoVo;
import com.example.storage.modules.user.entity.User;

public interface IBackService {

    UserInfo selectUserByEmail(String email);

    UserInfo selectUserByUserName(String username);

    UserInfo selectUserByUserId(String id);

    void chageUserPwd(String newPwd, String uid);

    Integer userRegister(UserInfo userInfo);

    Page<SelectManagerUserInfo> selectManagerUserList(SelectByTemplateDto dto);


    void updateUserStateByValidate(UserInfo userInfo);

    void updatePasswordByEmail(String email, String password);

    void updateUserField(UserInfo userInfo);
    
    void updateUserEmail(UserInfo userInfo);

    boolean validate(UserInfo userInfo, String password);
    
    
    //注册邮箱相关
    Integer insertMailValidate(MailValidate mailValidate);

    MailValidate selectMailValidateByEmail(String targetMail);

    void updateMailValidateByEmail(MailValidate mailValidate);

    void updateMailValidateStateByEmail(MailValidate mailValidate);

    void updateUseremail(String oldEmail, String newEmail);
    
    //管理员登录token生成
    String generateToken(UserInfo userInfo);

    boolean validateToken(String uuid);

    void removeLoginListByUUID(String uuid);
    
    //sso
    List<UserInfoVo> getAllUserInfo();
    
    List<UserInfo> getUserByUid(String uid);
    
    //用户登录token生成
  	String generateToken(User user);
  	

  	
  	//--------------------------------------------------------
  	boolean add(AddManagerUserDTO dto);
  //查询角色列表
    Page<Role> selectRoleList(SelectByTemplateDto dto);
  //新增角色
    boolean addRole(AddRoleDto role);

    //更新角色
    boolean updateRole(AddRoleDto role);

    //查询所有角色名
    public List<String> selectAllRoleName() ;
    //角色详情(根据id查询)
    Role selectRoleById(String id);
    
    String getPermissionByRoleName(String roleName);
    
    //更新用户
    boolean update(AddManagerUserDTO dto);
}
