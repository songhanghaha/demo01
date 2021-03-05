package com.example.storage.modules.user.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.user.pojo.po.SmsListPO;
import com.example.storage.modules.user.pojo.vo.SelectSmsByConditionsDTO;

public interface ISmsService {
   //根据手机号获取短信记录分页列表
   Page<SmsListPO> selectSmsList(SelectSmsByConditionsDTO dto);
}
