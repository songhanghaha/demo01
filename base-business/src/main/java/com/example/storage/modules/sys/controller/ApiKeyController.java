package com.example.storage.modules.sys.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.sso.client.SSOAuthorizingRealm;
import com.example.storage.common.base.BaseController;
import com.example.storage.common.base.Response;
import com.example.storage.common.base.ReturnCode;
import com.example.storage.common.base.exception.BusException;
import com.example.storage.modules.sys.dao.ApiKeyPermissionDao;
import com.example.storage.modules.sys.entity.ApikeyInfo;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.po.SelectApiKeyInfo;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.service.IApiKeyService;
import com.example.storage.modules.sys.service.IBackService;
import com.google.gson.Gson;

import io.swagger.annotations.Api;

@Api(tags = "系统管理-权限管理")
@RestController
@RequestMapping(value = "manager/apikey")
public class ApiKeyController extends BaseController {
	
	private static Logger logger = LogManager.getLogger(ApiKeyController.class.getName());
	
	@Value("${apikey.max-life}")
	private int API_KEY_MAX_LIFE;

	@Autowired
	private IBackService backService;
	
	@Autowired
	private IApiKeyService apiKeyService;
	@Autowired
	private ApiKeyPermissionDao apikeyPermDao;
	
	
	/**
	 * apikey列表
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public Response list(SelectByTemplateDto dto) {
		if (null == dto || dto.getPageSize() <= 0) {
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		Page<SelectApiKeyInfo> page = apiKeyService.selectApiKeyInfoList(dto);
		return page == null ? Response.ok(new Page<>()) : Response.ok(page);
	}

	/**
	 * 添加ApiKeyPermissions
	 * 
	 * @param apiKey
	 * @param remarks     备注
	 * @param checkExpire 永不过期
	 * @param expire      过期时间
	 * @return
	 */

	@RequestMapping(value = "/saveApiKeyPermissions", method = RequestMethod.POST)
	public Response genApiKey(String uuid, String permissions) {
		logger.info("apikey/saveApiKeyPermissions: uuid is [{}], permissions is [{}]", uuid, permissions);
		try {
			Gson gson = new Gson();
			List list = gson.fromJson(permissions, List.class);
			apiKeyService.saveApiKey(uuid, String.join(",", list));
			return Response.ok();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusException(ReturnCode.BIZ_FAIL);
		}
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Response delete(String uuid) {
		if (StringUtils.isEmpty(uuid)) {
			logger.warn("apiKey delete : param is null");
			throw new BusException(ReturnCode.PARAMETER_ERROR);
		}
		logger.info("apiKey/delete uuid is " +  uuid);
		apiKeyService.delete(uuid);
		logger.info("apiKey delete success!");
		return Response.ok();
	}
	
	
	@RequestMapping(value = "/getPermByApiKey", method = RequestMethod.GET)
	public Response getPermByApiKey(String uuid) {
		logger.info("apikey/getPermByApiKey: uuid is [{}]", uuid);
		String permissionByApiKeys = apikeyPermDao.getPermissionByApiKey(uuid);
		List permLists = new ArrayList();
		if (StringUtils.isNotBlank(permissionByApiKeys)) {
			String[] permissionByApiKey = permissionByApiKeys.split(",");				
			if (permissionByApiKey.length <= 0) {
				throw new BusException(ReturnCode.BIZ_FAIL);
			}
			permLists.add(permissionByApiKey);			
		}
		logger.info("apikey/getPermByApiKey success!");
		return Response.ok(permLists);
	}
    
	/**
	 * 添加APIKEY和更新APIKEY
	 * 
	 * @param apiKey      key
	 * @param remarks     备注
	 * @param checkExpire 永不过期，true：永不过期，false：有过期时间
	 * @param expire      过期时间
	 * @return
	 */
	
	@RequestMapping(value = "/handle", method = RequestMethod.POST)
	public Response apikeyHandle(String uuid, String remarks, boolean checkExpire,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date expire) {
		logger.info("apikey/handle: uuid is [{}], remarks is [{}], checkExpire is [{}], expire is [{}] ", uuid, remarks, checkExpire, expire);
        Subject subject = SecurityUtils.getSubject();
		SSOAuthorizingRealm.Principal principal = (SSOAuthorizingRealm.Principal) subject.getPrincipal();
		if (principal == null) {
			throw new BusException(ReturnCode.APIKEY_NOT_AUTH);
		}
		UserInfo userInfo = backService.selectUserByUserId(principal.getUid());
		if (userInfo == null) {
			throw new BusException(ReturnCode.APIKEY_INVALID_USER);
		}			
		if (checkExpire) {
			expire = new Date(2999, 00, 01);
		}
		if (expire.before(DateUtils.addSeconds(new Date(), API_KEY_MAX_LIFE))) {
			logger.warn("Expires later than current time ");
			throw new BusException(ReturnCode.APIKEY_ERROR_SET_EXPIRATION_TIME);

		}
		if (API_KEY_MAX_LIFE != -1 && expire.after(DateUtils.addSeconds(new Date(), API_KEY_MAX_LIFE))) {
			throw new BusException(ReturnCode.APIKEY_EXPIRE_LONG);
		}
		if (StringUtils.isEmpty(uuid)) {
			ApikeyInfo apiKeyInfo = apiKeyService.generate(userInfo, expire, remarks);
			logger.info("ApiKey generate success this ApiKey uuid is " + apiKeyInfo.getUuid());
			return Response.ok(apiKeyInfo);
		} else {
			ApikeyInfo byUuid = apiKeyService.findByUuid(uuid);
			if (byUuid != null) {
				byUuid.setExpire(expire);
				byUuid.setRemarks(remarks);
				apiKeyService.updata(byUuid);
				return Response.ok(byUuid);
			} else {
				logger.info("Not found this ApiKey the uuid is " + uuid);
				throw new BusException(ReturnCode.BIZ_FAIL);
			}
		}
	}
	
}
