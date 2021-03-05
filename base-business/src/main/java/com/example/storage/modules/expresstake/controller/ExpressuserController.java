package com.example.storage.modules.expresstake.controller;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.storage.common.base.BaseController;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.DELETE_STATE;
import com.example.storage.common.pojo.vo.BaseTemplateDto;
import com.example.storage.common.pojo.vo.DetailDto;
import com.example.storage.common.utils.IdGen;
import com.example.storage.modules.expresstake.dao.ExpressuserDao;
import com.example.storage.modules.expresstake.entity.Expressuser;
import com.example.storage.modules.expresstake.pojo.vo.ExpressuserVo;
import com.example.storage.modules.expresstake.service.ExpressuserService;
import com.example.storage.modules.wxuser.entity.Wxuser;
import com.example.storage.modules.wxuser.pojo.vo.WxuserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
/**
 * 用户信息
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
@Api(value = "用户信息模块", tags = "业务模块-用户信息-Expressuser")
@RestController
@RequestMapping(value = "manager/expressuser")
public class ExpressuserController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ExpressuserService service;
	@Autowired
	private ExpressuserDao dao;

	@ApiOperation(value = "用户信息列表")
	@RequestMapping(value = "list", method = RequestMethod.POST)
	public Response list(BaseTemplateDto dto) {
		log.info("ExpressuserController  list param is  " + dto.toString());
		return service.selectExpressuserList(dto);
	}

	@ApiOperation(value = "用户信息详情")
	@RequestMapping(value = "detail", method = RequestMethod.POST)
	public Response detail(DetailDto dto) {
	    log.info("ExpressuserController  detail   param is  " + dto.toString());
		Expressuser entity = dao.selectById(dto.getId());
		return Response.ok(entity);
	}


	@ApiOperation(value = "用户信息删除")
	@RequestMapping(value = "del", method = RequestMethod.POST)
	public Response del(DetailDto dto) {
	   log.info("ExpressuserController  del   param is  " + dto.toString());
		Expressuser entity = new Expressuser();
		entity.setId(dto.getId());
		entity.setIsdeleted(DELETE_STATE.ALREADY_DELETE.getCode());
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
	@ApiOperation(value = "用户信息新增")
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Response add(ExpressuserVo dto) {
		log.info("ExpressuserController   add param is  " + dto.toString());
		Expressuser entity =  new Expressuser();	
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
	
    @ApiOperation(value = "用户信息修改")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Response update(ExpressuserVo dto) {
	    log.info("ExpressuserController  update param is  " + dto.toString());
		Expressuser entity = new Expressuser();		
		BeanUtils.copyProperties(dto, entity);
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
	
	
	
	@ApiOperation(value = "总用户信息列表")
	@RequestMapping(value = "api/getAllExpressuser", method = RequestMethod.POST)
	public Response getExpressuser(@RequestBody DetailDto dto) {
		log.info("ExpressuserController  api/getExpressuser param is  ");
		List<Expressuser> list = service.getExpressusers(dto.getId());
		if (list == null || list.size() == 0) {
			return Response.ok(new ArrayList<Wxuser>());
		} else {
			return Response.ok(list);
		}
	}
    
    @ApiOperation(value = "用户信息详情")
	@RequestMapping(value = "api/detail", method = RequestMethod.POST)
	public Response apidetail(@RequestBody DetailDto dto) {
	    log.info("ExpressuserController  apidetail   param is  " + dto.toString());
		Expressuser entity = dao.selectById(dto.getId());
		return Response.ok(entity);
	}
    
	@ApiOperation(value = "用户信息新增")
	@RequestMapping(value = "api/add", method = RequestMethod.POST)
	public Response apiadd(@RequestBody WxuserVo dto) {
		log.info("ExpressuserController   add param is  " + dto.toString());
		Expressuser entity =  new Expressuser();	
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
	
    @ApiOperation(value = "用户信息修改")
	@RequestMapping(value = "api/update", method = RequestMethod.POST)
	public Response apiupdate(@RequestBody WxuserVo dto) {
	    log.info("ExpressuserController  apiupdate param is  " + dto.toString());
		Expressuser entity = new Expressuser();		
		BeanUtils.copyProperties(dto, entity);
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
    
    @ApiOperation(value = "用户信息删除")
	@RequestMapping(value = "api/del", method = RequestMethod.POST)
	public Response apidel(@RequestBody DetailDto dto) {
	   log.info("ExpressuserController  apidel   param is  " + dto.toString());
		Expressuser entity = new Expressuser();
		entity.setId(dto.getId());
		entity.setIsdeleted(DELETE_STATE.ALREADY_DELETE.getCode());
		entity.setUpdatetime(new Date());
		int i = dao.updateById(entity);
		if (i <= 0) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
		return Response.ok();
	}
}

