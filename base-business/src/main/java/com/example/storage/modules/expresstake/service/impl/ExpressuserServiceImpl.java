
package com.example.storage.modules.expresstake.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.Response;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.modules.expresstake.dao.ExpressuserDao;
import com.example.storage.modules.expresstake.entity.Expressuser;
import com.example.storage.modules.expresstake.service.ExpressuserService;

import net.sf.json.JSONObject;
/**
 * 用户信息ServiceImpl
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
@Service
public class ExpressuserServiceImpl implements ExpressuserService {

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ExpressuserDao expressuserDao;


	@Override
	public Response selectExpressuserList(BaseTemplateDto dto) {
		Page<Expressuser> page = new Page<Expressuser>(dto.getPageIndex(), dto.getPageSize());
		Wrapper<Expressuser> wrapper = new EntityWrapper<>();
	
        if (dto != null) {
			wrapper.eq("isdeleted", "0");
			wrapper.orderBy("createtime", false);
		}
		if (StringUtils.isNotEmpty(dto.getTemplate())) {
			JSONObject jsonObject = JSONObject.fromObject(dto.getTemplate());
			Expressuser ptable = (Expressuser) JSONObject.toBean(jsonObject, Expressuser.class);
			
			 if(StringUtils.isNotEmpty(ptable.getId())) {
			wrapper.eq("id", ptable.getId());
		} if(StringUtils.isNotEmpty(ptable.getName())) {
			wrapper.eq("name", ptable.getName());
		} if(StringUtils.isNotEmpty(ptable.getUsertype())) {
			wrapper.eq("usertype", ptable.getUsertype());
		} if(StringUtils.isNotEmpty(ptable.getTel())) {
			wrapper.eq("tel", ptable.getTel());
		} if(StringUtils.isNotEmpty(ptable.getPwd())) {
			wrapper.eq("pwd", ptable.getPwd());
		} if(StringUtils.isNotEmpty(ptable.getGender())) {
			wrapper.eq("gender", ptable.getGender());
		} if(StringUtils.isNotEmpty(ptable.getWorktime())) {
			wrapper.eq("worktime", ptable.getWorktime());
		} if(StringUtils.isNotEmpty(ptable.getVchartno())) {
			wrapper.eq("vchartno", ptable.getVchartno());
		}
			
		}
		
		List<Expressuser> list = this.expressuserDao.selectPage(page, wrapper);
		Integer total = this.expressuserDao.selectCount(wrapper);
		page.setRecords(list);
		page.setTotal(total);
		return Response.ok(page);
	}
	
	@Override
	public List<Expressuser> getExpressusers(String userid) {
		Wrapper<Expressuser> wrapper = new EntityWrapper<>();
		wrapper.eq("isdeleted", "0");
		wrapper.orderBy("createtime", false);
		List<Expressuser> mstters = this.expressuserDao.selectList(wrapper);
		return mstters;
	}
}

