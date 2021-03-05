package com.example.storage.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.vo.SelectManagerUserInfo;
import com.example.storage.modules.sys.pojo.vo.back.UserInfoVo;

@Mapper
public interface UserInfoDao extends BaseMapper<UserInfo> {
    List<UserInfoVo> getAllUserInfo();

    UserInfo selectUserByEmail(@Param("email") String email);

    UserInfo selectUserByUserName(@Param("username") String username);

    UserInfo selectUserByUserId(@Param("id") String id);

    void chageUserPwd(@Param("password") String newPwd, @Param("id") String uid);

    Integer userRegister(UserInfo userInfo);

    void updateUserStateByValidate(UserInfo userInfo);

    void update(UserInfo userInfo);

    void updateUserField(UserInfo userInfo);
    
    //void updateUserApiToken(UserInfo userInfo);
    
    void updateUserEmail(UserInfo userInfo);
    ///根据key和秘钥查询用户
    UserInfo selectUserByApikeyAndSecret(@Param("apiKey") String apiKey, @Param("apiKeySecret") String apiKeySecret);

    /**
     * tsp manager 查询管理员
     */
    List<SelectManagerUserInfo> selectValidList(Page page, @Param("isDeleted") String isDeleted);
    List<SelectManagerUserInfo> selectValidListByTemplate(Page page,
                                                   @Param("template") String template,
                                                   @Param("isDeleted") String isDeleted);
    int selectValidCount(@Param("isDeleted")String isDeleted);
    int selectValidCountByTemplate(@Param("template") String template,
                                   @Param("isDeleted")String isDeleted);
}
