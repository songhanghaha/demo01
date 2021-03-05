package com.example.storage.modules.sys.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.modules.sys.entity.ApikeyInfo;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.po.SelectApiKeyInfo;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;

public interface IApiKeyService {
    ApikeyInfo generate(UserInfo user, Date expire, String remarks);

    List<ApikeyInfo> selectApiKeyListByUserId(String uid);

    void delete(String uuid);

    void updata(ApikeyInfo apiKeyInfo);

    ApikeyInfo findByUuid(String apiKey);

    Page<SelectApiKeyInfo> selectApiKeyInfoList(SelectByTemplateDto dto);
	
	void saveApiKey(String apikey, String permissions);
	
}
