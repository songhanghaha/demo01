package com.example.storage.modules.sys.security;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.sso.client.SSOAuthorizingRealm;
import com.example.storage.modules.sys.dao.ApiKeyPermissionDao;
import com.example.storage.modules.sys.entity.ApikeyPermission;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.service.IBackService;
import com.example.storage.modules.user.dao.UserDao;
import com.example.storage.modules.user.entity.User;
import com.google.gson.Gson;

@Service
public class SSORealm extends SSOAuthorizingRealm {

	@Autowired
	private ApiKeyPermissionDao apiKeyPermissionDao;
	@Autowired
	private IBackService backService;

	@Autowired 
	private UserDao userDao;
	/**
	 * APIKEY
	 */
	@Override
	protected void doGetApiKeyAuthorizationInfo(String apiKey, Set<String> permissions) {
		List<ApikeyPermission> list = apiKeyPermissionDao.getUserApikeys(apiKey);
		if (null != list && list.size() > 0) {
			ApikeyPermission apikeyPermission = list.get(0);
			if (null != apikeyPermission && StringUtils.isNotEmpty(apikeyPermission.getPermissions())) {
				permissions.addAll(Arrays.asList(apikeyPermission.getPermissions().split(",")));
			}
		}
	}

	/**
     * USER TOKEN
     */
	@Override
	protected void doGetUserAuthorizationInfo(String uid, Set<String> roles, Set<String> permissions) {
		UserInfo userInfo = backService.selectUserByUserId(uid);
		if (userInfo != null) {
			String perms = backService.getPermissionByRoleName(userInfo.getRoles());
			if (StringUtils.isNotEmpty(perms)) {
				permissions.addAll(new Gson().fromJson(perms, List.class));
			}
			roles.add(userInfo.getRoles());
		} else {
			User user = userDao.selectUserById(uid);
			if (user != null) {
				roles.add("vehicle_owner");
				String perms = backService.getPermissionByRoleName("vehicle_owner");
				if (StringUtils.isNotEmpty(perms)) {
					permissions.addAll(new Gson().fromJson(perms, List.class));
				}
			}
		}
	}
}
