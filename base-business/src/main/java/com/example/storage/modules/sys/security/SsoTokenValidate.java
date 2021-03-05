package com.example.storage.modules.sys.security;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.example.sso.client.ApiKeyEntity;
import com.example.sso.client.SSOConnector;
import com.example.storage.modules.sys.entity.ApikeyInfo;
import com.example.storage.modules.sys.service.IApiKeyService;

@Component
@Primary
public class SsoTokenValidate implements SSOConnector {

	@Autowired
	private IApiKeyService apikeyService;

	@Override
	public boolean checkValidity(String uuid) {
		/**
		 * 展示删除代码 后续会启用
		 * boolean b = apikeyService.validateToken(uuid);
		 *  return b;
		 */
		return true;
	}

	@Override
	public boolean checkApiKeyValidity(ApiKeyEntity apiKeyEntity) {
		ApikeyInfo apikeyInfo = apikeyService.findByUuid(apiKeyEntity.getApiKey());
		if (null != apikeyInfo && StringUtils.isNotEmpty(apikeyInfo.getUid())) {
			apiKeyEntity.setExpire(apikeyInfo.getExpire());
			apiKeyEntity.setUid(apikeyInfo.getUid());
			return true;
		}
		return false;
	}
}
