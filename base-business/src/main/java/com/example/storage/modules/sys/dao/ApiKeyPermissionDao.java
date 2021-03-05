package com.example.storage.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.example.storage.modules.sys.entity.ApikeyPermission;

@Mapper
public interface ApiKeyPermissionDao extends BaseMapper<ApikeyPermission> {

    String getPermissionByApiKey(@Param("apikey") String apikey);
    
    List<ApikeyPermission> getUserApikeys(@Param("apikey") String apikey);
    
    ApikeyPermission validateApiKey(@Param("apikey") String apikey);

    void updateApiKey(@Param("apikey") String apikey, @Param("permissions") String permissions);

    void saveApiKey(@Param("apikey") String apikey, @Param("permissions") String permissions);

    void deleteApiKey(String apikey); 
   
}
