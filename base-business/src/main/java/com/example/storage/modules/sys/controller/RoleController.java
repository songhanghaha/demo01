package com.example.storage.modules.sys.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import com.example.storage.modules.sys.entity.Role;
import com.example.storage.modules.sys.pojo.vo.AddRoleDto;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.service.IBackService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "角色管理", tags = "角色管理")
@RestController
@RequestMapping("manager/role")
public class RoleController extends BaseController {

    @Autowired
    private IBackService backService;

    @ApiOperation(value = "角色列表", notes = "角色列表")
   // @RequiresPermissions(value = "role:list")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Response UserList(SelectByTemplateDto dto) {
        if (null == dto || dto.getPageSize() <= 0) {
            logger.warn("role List : param is error, param is " + dto);
            
            throw new BusException(ReturnCode.PARAMETER_ERROR);
        }
        Page<Role> page = backService.selectRoleList(dto);
        return page == null ? Response.ok(new Page<>()) : Response.ok(page);
    }

    //@RequiresPermissions(value = "role:add")
    @ApiOperation(value = "新增角色", notes = "角色列表")
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public Response addRole(AddRoleDto role) {
        if (StringUtils.isEmpty(role.getRoleName()) || StringUtils.isEmpty(role.getPermissions())) {
            logger.warn("role add : param is error, param is " + role);
            throw new BusException(ReturnCode.PARAMETER_ERROR);
        }
        if (backService.addRole(role)) {
            return Response.ok();
        } else {
            throw new BusException(ReturnCode.BIZ_FAIL);
        }
    }

    @ApiOperation(value = "更新角色", notes = "更新角色")
    //@RequiresPermissions(value = "role:update")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Response updateRole(AddRoleDto role) {
        if (StringUtils.isEmpty(role.getRoleName()) || StringUtils.isEmpty(role.getPermissions())) {
            logger.warn("role update : param is error, param is " + role);
            throw new BusException(ReturnCode.PARAMETER_ERROR);
        }
        if (backService.updateRole(role)) {
            return Response.ok();
        } else {
            throw new BusException(ReturnCode.BIZ_FAIL);
        }
    }

    @ApiOperation(value = "角色详情", notes = "角色详情")
    //@RequiresPermissions(value = "role:detail")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public Response detail(BaseDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            logger.warn("role detail : param is error, param is " + dto);
            throw new BusException(ReturnCode.PARAMETER_ERROR);
        }
        Role role = backService.selectRoleById(dto.getId());
        if (role != null) {
            return Response.ok(role);
        } else {
            throw new BusException(ReturnCode.SELECT_RESULT_NULL);
        }
    }

    @ApiOperation(value = "角色列表", notes = "角色列表")
    @RequestMapping(value = "allRoleName", method = RequestMethod.GET)
    public Response allRoleName() {
        List<String> list = backService.selectAllRoleName();
        return list != null && list.size() != 0 ? Response.ok(list) : Response.ok(new ArrayList<String>());
    }
}
