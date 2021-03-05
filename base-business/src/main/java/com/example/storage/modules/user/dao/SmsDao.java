package com.example.storage.modules.user.dao;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.user.entity.Sms;
import com.example.storage.modules.user.pojo.po.SmsListPO;

@Mapper
public interface SmsDao extends BaseMapper<Sms> {
	
    List<SmsListPO> selectSmsList(Page page, @Param("phoneNumber") String phoneNumber, @Param("type") String type);
    
    int selectCount(@Param("phoneNumber") String phoneNumber, @Param("type") String type);
}
