
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
import com.example.storage.modules.expresstake.dao.ExpressinfDao;
import com.example.storage.modules.expresstake.entity.Expressinf;
import com.example.storage.modules.expresstake.service.ExpressinfService;

import net.sf.json.JSONObject;
/**
 * 快递信息ServiceImpl
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
@Service
public class ExpressinfServiceImpl implements ExpressinfService {

	public Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ExpressinfDao expressinfDao;


	@Override
	public Response selectExpressinfList(BaseTemplateDto dto) {
		Page<Expressinf> page = new Page<Expressinf>(dto.getPageIndex(), dto.getPageSize());
		Wrapper<Expressinf> wrapper = new EntityWrapper<>();
	
        if (dto != null) {
			wrapper.eq("isdeleted", "0");
			wrapper.orderBy("createtime", false);
		}
		if (StringUtils.isNotEmpty(dto.getTemplate())) {
			JSONObject jsonObject = JSONObject.fromObject(dto.getTemplate());
			Expressinf ptable = (Expressinf) JSONObject.toBean(jsonObject, Expressinf.class);
			
			 if(StringUtils.isNotEmpty(ptable.getId())) {
			wrapper.eq("id", ptable.getId());
		} if(StringUtils.isNotEmpty(ptable.getReceiver())) {
			wrapper.eq("receiver", ptable.getReceiver());
		} if(StringUtils.isNotEmpty(ptable.getAddress())) {
			wrapper.eq("address", ptable.getAddress());
		} if(StringUtils.isNotEmpty(ptable.getReceivetel())) {
			wrapper.eq("receivetel", ptable.getReceivetel());
		} if(StringUtils.isNotEmpty(ptable.getExpressno())) {
			wrapper.eq("expressno", ptable.getExpressno());
		} if(StringUtils.isNotEmpty(ptable.getFloorr())) {
			wrapper.eq("floorr", ptable.getFloorr());
		} if(StringUtils.isNotEmpty(ptable.getCourierid())) {
			wrapper.eq("courierid", ptable.getCourierid());
		} if(StringUtils.isNotEmpty(ptable.getCouriername())) {
			wrapper.eq("couriername", ptable.getCouriername());
		} if(StringUtils.isNotEmpty(ptable.getCouriergender())) {
			wrapper.eq("couriergender", ptable.getCouriergender());
		} if(StringUtils.isNotEmpty(ptable.getPrice())) {
			wrapper.eq("price", ptable.getPrice());
		} if(StringUtils.isNotEmpty(ptable.getCollectid())) {
			wrapper.eq("collectid", ptable.getCollectid());
		} if(StringUtils.isNotEmpty(ptable.getCollectname())) {
			wrapper.eq("collectname", ptable.getCollectname());
		} if(StringUtils.isNotEmpty(ptable.getCollecttel())) {
			wrapper.eq("collecttel", ptable.getCollecttel());
		} if(StringUtils.isNotEmpty(ptable.getCollectfloor())) {
			wrapper.eq("collectfloor", ptable.getCollectfloor());
		} if(StringUtils.isNotEmpty(ptable.getCollectdoorno())) {
			wrapper.eq("collectdoorno", ptable.getCollectdoorno());
		} if(StringUtils.isNotEmpty(ptable.getCollectprice())) {
			wrapper.eq("collectprice", ptable.getCollectprice());
		} if(StringUtils.isNotEmpty(ptable.getCollecttime())) {
			wrapper.eq("collecttime", ptable.getCollecttime());
		} if(StringUtils.isNotEmpty(ptable.getUserid())) {
			wrapper.eq("userid", ptable.getUserid());
		}
			
		}
		
		List<Expressinf> list = this.expressinfDao.selectPage(page, wrapper);
		Integer total = this.expressinfDao.selectCount(wrapper);
		page.setRecords(list);
		page.setTotal(total);
		return Response.ok(page);
	}
	
	@Override
	public List<Expressinf> getExpressinfs(String userid) {
		Wrapper<Expressinf> wrapper = new EntityWrapper<>();
		wrapper.eq("isdeleted", "0");
		wrapper.orderBy("createtime", false);
		List<Expressinf> mstters = this.expressinfDao.selectList(wrapper);
		return mstters;
	}
}

