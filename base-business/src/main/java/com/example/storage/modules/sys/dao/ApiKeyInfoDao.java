package com.example.storage.modules.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.entity.ApikeyInfo;
import com.example.storage.modules.sys.pojo.po.SelectApiKeyInfo;

@Mapper
public interface ApiKeyInfoDao extends BaseMapper<ApikeyInfo> {
    void save(ApikeyInfo apiKey);

    void update(ApikeyInfo apiKeyInfo);

    void deleteByUidUuid(@Param("uuid") String uuid);

    ApikeyInfo findByUuid(@Param("uuid") String uuid);

    List<ApikeyInfo> listByUid(@Param("uid") String uid);

    //管理平台查询ApiKey 
    List<SelectApiKeyInfo> selectValidListByTemplate(Page page, @Param("template") String template, @Param("isDeleted") String isDeleted);
    
    int selectValidCountByTemplate(@Param("template") String template, @Param("isDeleted")String isDeleted);
}
