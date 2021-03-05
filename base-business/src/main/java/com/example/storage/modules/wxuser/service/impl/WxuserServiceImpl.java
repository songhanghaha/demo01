
package com.example.storage.modules.wxuser.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.Response;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.modules.wxuser.dao.WxuserDao;
import com.example.storage.modules.wxuser.entity.Wxuser;
import com.example.storage.modules.wxuser.service.WxuserService;
/**
 * 微信用户ServiceImpl
 * @author frank
 * @datatime 2020-11-15 09:13:17
 */
@Service
public class WxuserServiceImpl implements WxuserService {

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private WxuserDao wxuserDao;


	@Override
	public Response selectWxuserList(BaseTemplateDto dto) {
		Page<Wxuser> page = new Page<Wxuser>(dto.getPageIndex(), dto.getPageSize());
		Wrapper<Wxuser> wrapper = new EntityWrapper<>();
		if (dto != null) {
			wrapper.eq("isdeleted", "0");
		}
		wrapper.orderBy("createtime", false);
		List<Wxuser> list = this.wxuserDao.selectPage(page, wrapper);
		Integer total = this.wxuserDao.selectCount(wrapper);
		page.setRecords(list);
		page.setTotal(total);
		return Response.ok(page);
	}
	
	@Override
	public List<Wxuser> getAllWxUser(String userid) {
		Wrapper<Wxuser> wrapper = new EntityWrapper<>();
		wrapper.eq("isdeleted", "0");
		wrapper.orderBy("createtime", false);
		List<Wxuser> mstters = this.wxuserDao.selectList(wrapper);
		return mstters;
	}
}

