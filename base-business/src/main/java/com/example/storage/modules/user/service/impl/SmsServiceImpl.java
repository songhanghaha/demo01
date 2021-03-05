package com.example.storage.modules.user.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.user.dao.SmsDao;
import com.example.storage.modules.user.pojo.po.SmsListPO;
import com.example.storage.modules.user.pojo.vo.SelectSmsByConditionsDTO;
import com.example.storage.modules.user.service.ISmsService;

@Service
public class SmsServiceImpl implements ISmsService {
	
	@Autowired
	private SmsDao smsDao;

	@Override
	public Page<SmsListPO> selectSmsList(SelectSmsByConditionsDTO dto) {
		Page<SmsListPO> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
		List<SmsListPO> list = smsDao.selectSmsList(page, StringUtils.trim(dto.getPhoneNumber()), dto.getType());
		int count = smsDao.selectCount(StringUtils.trim(dto.getPhoneNumber()), dto.getType());
		page.setRecords(list);
		page.setTotal(count);
		return page;
	}

}
