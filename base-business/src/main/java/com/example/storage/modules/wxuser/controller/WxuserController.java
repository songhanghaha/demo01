package com.example.storage.modules.wxuser.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.example.storage.common.base.BaseController;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.DELETE_STATE;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.common.pojo.vo.DetailDto;
import com.example.storage.common.utils.HttpUtils;
import com.example.storage.common.utils.IdGen;
import com.example.storage.modules.wxuser.dao.WxuserDao;
import com.example.storage.modules.wxuser.entity.Wxuser;
import com.example.storage.modules.wxuser.pojo.vo.WxuserVo;
import com.example.storage.modules.wxuser.service.WxuserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 微信用户
 * @author frank
 * @datatime 2020-11-15 09:13:17
 */
@Api(value = "微信用户模块", tags = "业务模块-微信用户-Wxuser")
@RestController
@RequestMapping(value = "manager/wxuser")
public class WxuserController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WxuserService service;
	@Autowired
	private WxuserDao dao;

	@ApiOperation(value = "微信用户列表")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Response list(BaseTemplateDto dto) {
		log.info("WxuserController  list param is  " + dto.toString());
		return service.selectWxuserList(dto);
	}

	@ApiOperation(value = "微信用户详情")
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public Response detail(DetailDto dto) {
	    log.info("WxuserController  detail   param is  " + dto.toString());
		Wxuser entity = dao.selectById(dto.getId());
		return Response.ok(entity);
	}


	@ApiOperation(value = "微信用户删除")
	@RequestMapping(value = "del", method = RequestMethod.POST)
	public Response del(DetailDto dto) {
	   log.info("WxuserController  del   param is  " + dto.toString());
		Wxuser entity = new Wxuser();
		entity.setId(dto.getId());
		entity.setIsdeleted(DELETE_STATE.ALREADY_DELETE.getCode());
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
	@ApiOperation(value = "微信用户新增")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Response add(WxuserVo dto) {
		log.info("WxuserController   add param is  " + dto.toString());
		Wxuser entity =  new Wxuser();	
		BeanUtils.copyProperties(dto, entity);  
	    entity.setId(IdGen.uuid());
	    entity.setIsdeleted(DELETE_STATE.NOT_DELETE.getCode());	    
		entity.setCreatetime(new Date());
		int i = dao.insert(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.INSERT_FAIL);
		}
		return Response.ok();
	}	
	
    @ApiOperation(value = "微信用户修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Response update(WxuserVo dto) {
	    log.info("WxuserController  update param is  " + dto.toString());
		Wxuser entity = new Wxuser();		
		BeanUtils.copyProperties(dto, entity);
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
    
    
    @ApiOperation(value = "微信用户新增")
	@RequestMapping(value = "binWxUser", method = RequestMethod.POST)
	public Response binWxUser(@RequestBody WxuserVo dto) {
		log.info("WxuserController   add param is  " + dto.toString());		
		String openid=dto.getOpenid();
		if(StringUtils.isNotBlank(openid)) {
			Wrapper<Wxuser> wrapper = new EntityWrapper<>();
			if (openid != null) {
				wrapper.eq("isdeleted", "0");
			}
			wrapper.eq("openid", openid);
			wrapper.orderBy("createtime", false);			
			List<Wxuser> list = dao.selectList(wrapper);
			if(null!=list&&list.size()>0) {
				Wxuser d=list.get(0);
				
				Wxuser entity =  new Wxuser();	
				entity.setId(d.getId());
				entity.setUpdatetime(new Date());				
				int i = dao.updateById(entity);
				if (i <= 0) {
					throw new BusException(ReturnCode.UPDATE_ERROR);
				}
				return Response.ok(d.getId());
			}else {
				Wxuser entity =  new Wxuser();	
				BeanUtils.copyProperties(dto, entity);  
			    entity.setId(IdGen.uuid());
			    entity.setIsdeleted(DELETE_STATE.NOT_DELETE.getCode());	    
				entity.setCreatetime(new Date());
				int i = dao.insert(entity);
				if (i <= 0) {
					throw new BusException(ReturnCode.INSERT_FAIL);
				}
				return Response.ok(entity.getId());
			}
		}else {
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}		
	}	
    
	@ApiOperation(value = "微信用户详情")
	@RequestMapping(value = "getuserByOpenid", method = RequestMethod.POST)
	public Response getUserByOpenId(@RequestBody DetailDto dto) {
	    log.info("WxuserController  detail   param is  " + dto.toString());
	    Wrapper<Wxuser> wrapper = new EntityWrapper<>();
		wrapper.eq("openid", dto.getId());
		wrapper.eq("isdeleted", "0");
		wrapper.orderBy("createtime", false);			
		List<Wxuser> list = dao.selectList(wrapper);
		if(null!=list&&list.size()>0) {
			Wxuser d=list.get(0);			
			return Response.ok(d);
		}else {
			return Response.ok(null);
		}
	}
	
    @ApiOperation(value = "获得openid")
	@RequestMapping(value = "getSessionKey", method = RequestMethod.POST)
	public Response getSessionKey(@RequestBody DetailDto dto) {
	    log.info("WxuserController  update param is  " + dto.toString());
	    String AppID = "wxa3dc8766b378689e";
		String secret = "35b0cf179bedbc49b13483ed589a7b1b";
		JSONObject json = getSessionKey(AppID, secret, dto.getId());
		// {"session_key":"IpXoqqVheF8BDnDeWiyCtg==","expires_in":7200,"openid":"owJT80Pd9eUvXXCFTVKOzhLY9pCI"}
		return Response.ok(json);
	}
    
    
    public JSONObject getSessionKey(String AppID, String secret, String code) {
		// String url = String
		// .format("https://api.weixin.qq.com/sns/jscode2session?appid={0}&secret={1}&js_code={2}&grant_type=authorization_code",
		// AppID, secret, code);
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
				+ AppID + "&secret=" + secret + "&js_code=" + code
				+ "&grant_type=authorization_code";
		JSONObject jsonObject=new JSONObject();
		String response;
		JSONObject json=null ;
		try {
			response = HttpUtils.sendPost(url, jsonObject, HttpUtils.charset);
			 json = JSONObject.parseObject(response);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		//.httpPost(url, entity);
		// String openId = "";
		// String access_token = "";
		// int expiresIn = 7200;
		// String refresh_token = "";
		// String scope = "";
		// String unionid = "";
		// try {
		// access_token = json.getString("access_token");
		// expiresIn = json.getInt("expires_in");
		// refresh_token = json.getString("refresh_token");
		// openId = json.getString("openid");
		// scope = json.getString("scope");
		// if (json.containsKey("unionid") == true) {
		// unionid = json.getString("unionid");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// companyId
		return json;
	}
    
    
    
    /*****************框架测试用代码-start*******************************/
    @ApiOperation(value = "总微信用户列表")
	@RequestMapping(value = "getAllWxUser", method = RequestMethod.POST)
	public Response getAllWxUser(@RequestBody DetailDto dto) {
		log.info("WxuserController  getAllWxUser param is  ");
		List<Wxuser> list = service.getAllWxUser(dto.getId());
		log.info("WxuserController  getAllWxUser list is  "+list);
		if (list == null || list.size() == 0) {
			return Response.ok(new ArrayList<Wxuser>());
		} else {
			return Response.ok(list);
		}
	}
    
    @ApiOperation(value = "微信用户详情")
	@RequestMapping(value = "wxdetail", method = RequestMethod.POST)
	public Response wxdetail(@RequestBody DetailDto dto) {
	    log.info("WxuserController  detail   param is  " + dto.toString());
		Wxuser entity = dao.selectById(dto.getId());
		return Response.ok(entity);
	}
    
	@ApiOperation(value = "微信用户新增")
	@RequestMapping(value = "wxadd", method = RequestMethod.POST)
	public Response wxadd(@RequestBody WxuserVo dto) {
		log.info("WxuserController   add param is  " + dto.toString());
		Wxuser entity =  new Wxuser();	
		BeanUtils.copyProperties(dto, entity);  
	    entity.setId(IdGen.uuid());
	    entity.setIsdeleted(DELETE_STATE.NOT_DELETE.getCode());	    
		entity.setCreatetime(new Date());
		int i = dao.insert(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.INSERT_FAIL);
		}
		return Response.ok();
	}	
	
    @ApiOperation(value = "微信用户修改")
	@RequestMapping(value = "wxupdate", method = RequestMethod.POST)
	public Response wxupdate(@RequestBody WxuserVo dto) {
	    log.info("WxuserController  update param is  " + dto.toString());
		Wxuser entity = new Wxuser();		
		BeanUtils.copyProperties(dto, entity);
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
    
    @ApiOperation(value = "微信用户删除")
	@RequestMapping(value = "wxdel", method = RequestMethod.POST)
	public Response wxdel(@RequestBody DetailDto dto) {
	   log.info("WxuserController  del   param is  " + dto.toString());
		Wxuser entity = new Wxuser();
		entity.setId(dto.getId());
		entity.setIsdeleted(DELETE_STATE.ALREADY_DELETE.getCode());
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
    /*****************框架测试用代码-end*******************************/
	
}

