package com.example.storage.modules.user.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.base.BaseController;
import com.example.storage.common.base.BaseDTO;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.common.config.enums.UPDATE_DATA;
import com.example.storage.modules.sys.pojo.po.SelectCertInfoByCertSnPO;
import com.example.storage.modules.sys.pojo.po.SelectVehicleListByUserPO;
import com.example.storage.modules.sys.pojo.vo.DeviceByUserIdPO;
import com.example.storage.modules.sys.pojo.vo.MobileUpdateDTO;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.pojo.vo.SelectUserListPO;
import com.example.storage.modules.sys.pojo.vo.SelectUserVehicleMappingDto;
import com.example.storage.modules.sys.pojo.vo.UserAddDTO;
import com.example.storage.modules.sys.pojo.vo.UserIsTestDTO;
import com.example.storage.modules.sys.pojo.vo.UserStatusDTO;
import com.example.storage.modules.sys.pojo.vo.UserUpdateDTO;
import com.example.storage.modules.sys.security.SSORealm;
import com.example.storage.modules.user.dao.UserDao;
import com.example.storage.modules.user.entity.User;
import com.example.storage.modules.user.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * 用户管理
 */

@Api(value = "用户管理", tags = "用户管理")
@RestController
@RequestMapping(value = "manager/user")
public class UserManagerController extends BaseController {

	@Autowired
	private IUserService userService;
	@Autowired
	private UserDao userDao;

	private Logger logger = LoggerFactory.getLogger(UserManagerController.class);
	@RequiresRoles(value = {"normal_user"})
	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public Response newPwd(String email, String oldPass,String qq,String declaration,
			String birthday) {
		try {
			String uid = ((SSORealm.Principal) SecurityUtils.getSubject().getPrincipal()).getUid();
			if (StringUtils.isEmpty(oldPass)) {
				logger.warn("updateUser nickname : email is " + email);
				return Response.paramError();
			}
			User user = new User();
			user.setId(uid);
			user.setQq(qq);
			user.setBirthday(birthday);
			user.setDeclaration(declaration);
			user.setUserName(oldPass);
			userDao.updateById(user);
			return Response.ok();
		} catch (Exception ex) {
			logger.error("newPwd error is ", ex);
			return Response.systemError();
		}
	}

	@ApiOperation(value = "用户信息添加", notes = "用户创建")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Response UserAdd(UserAddDTO userDTO) {
		if (StringUtils.isBlank(userDTO.getMobile())) {
			logger.warn("UserAdd param is null");
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		if (userService.addUser(userDTO)) {
			return Response.ok();
		} else {
			throw new BusException(ReturnCode.BIZ_FAIL);
		}
	}

	@ApiOperation(value = "更换手机", notes = "用户信息修改")
	@RequiresRoles(value = { "super_admin" })
	@RequestMapping(value = "updateMobile", method = RequestMethod.POST)
	public Response updateMobile(MobileUpdateDTO dto) {
		if (dto == null || StringUtils.isEmpty(dto.getId()) || StringUtils.isEmpty(dto.getMobile())) {
			logger.warn("updateMobile : param is null , " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		if (userService.updateMobile(dto)) {
			return Response.ok();
		} else {
			throw new BusException(ReturnCode.BIZ_FAIL);
		}
	}

	@ApiOperation(value = "用户信息删除", notes = "用户信息删除")
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Response UserDelete(BaseDTO dto) {
		if (dto == null || StringUtils.isEmpty(dto.getId())) {
			logger.warn("UserDelete : param is null , " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		return userService.deleteUser(dto.getId()) == 1 ? Response.ok() : Response.updateFailed();
	}

	@ApiOperation(value = "用户信息详情", notes = "用户详情：用户基础信息")
	//@RequiresRoles(value = { "super_admin","normal_user"  })
	@RequestMapping(value = "selectUserDetailById", method = RequestMethod.POST)
	public Response UserDetail(@ApiParam(name = "id", value = "用户ID", required = true) String id,String mobile) {
		User user =null;
		if(StringUtils.isEmpty(id)) {
			user= userService.selectByMobile(mobile);
		}else {
			user = userService.selectUserDetailById(id);		
		}		
		if (user == null) {
			throw new BusException(ReturnCode.SELECT_RESULT_NULL);
		}
		return Response.ok(user);
	}

	@ApiOperation(value = "用户启用禁用", notes = "用户详情：启用或禁用按钮")
	@RequiresRoles(value = { "super_admin" })
	@RequestMapping(value = "status", method = RequestMethod.POST)
	public Response status(UserStatusDTO user) {
		if (user == null || StringUtils.isBlank(user.getStatus()) || StringUtils.isBlank(user.getId())) {
			logger.warn("User status : param is null , " + user);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		String status = userService.updateStatus(user);
		if (StringUtils.isNotEmpty(status)) {
			return Response.ok(status);
		} else {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		}
	}

	@ApiOperation(value = "模糊查询用户信息", notes = "用户管理：用户列表")

	@RequestMapping(value = "selectUserListByTemplate", method = RequestMethod.POST)
	public Response selectUserListByTemplate(SelectByTemplateDto dto) {
		if (dto == null || dto.getPageSize() <= 0) {
			logger.warn("selectUserListByTemplate : param is error");
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<SelectUserListPO> page = userService.selectUserListByTemplate(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}

	@ApiOperation(value = "查询用户关联的所有人车关系", notes = "用户详情：车辆列表")
	@RequiresRoles(value = { "super_admin" })
	@RequestMapping(value = "selectVehicleListByUserId", method = RequestMethod.POST)
	public Response selectDkListByVin(SelectUserVehicleMappingDto dto) {
		if (dto == null || dto.getPageSize() <= 0 || StringUtils.isBlank(dto.getUserId())) {
			logger.warn("selectVehicleListByUserId : param is null , " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<SelectVehicleListByUserPO> page = null;// userService.selectUserVehicleMappingByUserId(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}

	@ApiOperation(value = "根据用户信息查询关联的终端列表", notes = "用户详情：终端列表")
	@RequiresRoles(value = { "super_admin" })
	@RequestMapping(value = "selectDeviceListByUserId", method = RequestMethod.POST)
	public Response selectDeviceListByUserId(SelectUserVehicleMappingDto dto) {
		if (dto == null || StringUtils.isBlank(dto.getUserId())) {
			logger.warn("selectDeviceListByUserId : param is null , " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<DeviceByUserIdPO> page = userService.selectDeviceListByUserId(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}

	@ApiOperation(value = "根据证书序列号查询证书详情", notes = "车辆详情、终端详情：查看证书详情按钮")
	@RequiresRoles(value = { "super_admin", "normal_user" }, logical = Logical.OR)
	@RequestMapping(value = "selectCertInfoByCertSn", method = RequestMethod.POST)
	public Response selectCertInfoByCertSn(CertSnDTO dto) {
		if (dto == null || StringUtils.isBlank(dto.getCertSn())) {
			logger.warn("selectCertInfoByCertSn : param is null ," + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		SelectCertInfoByCertSnPO po = userService.selectCertInfoByCertSn(dto.getCertSn());
		if (po == null) {
			throw new BusException(ReturnCode.SELECT_RESULT_NULL);
		}
		return Response.ok(po);
	}

	@Data
	public class CertSnDTO {
		private String certSn;
	}

	@ApiOperation(value = "设置存储空间大小")
	//@RequiresRoles(value = { "super_admin" })
	@RequestMapping(value = "updateTestPermission", method = RequestMethod.POST)
	public Response updateTestPermission(UserIsTestDTO dto) {
		if (dto == null || StringUtils.isBlank(dto.getUserId()) || StringUtils.isBlank(dto.getIsTestUser())) {
			logger.warn("updateTestPermission : param is null , " + dto);
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		int update = userService.updateIsTestUser(dto);
		if (update != UPDATE_DATA.SUCCESS.getCode()) {
			throw new BusException(ReturnCode.UPDATE_ERROR);
		} else {
			return Response.ok();
		}
	}

}
