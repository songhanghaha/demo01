package com.example.storage.modules.sys.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.example.storage.common.config.enums.DELETE_STATE;
import com.example.storage.common.utils.IdGen;
import com.example.storage.modules.sys.dao.ApiKeyInfoDao;
import com.example.storage.modules.sys.dao.ApiKeyPermissionDao;
import com.example.storage.modules.sys.entity.ApikeyInfo;
import com.example.storage.modules.sys.entity.ApikeyPermission;
import com.example.storage.modules.sys.entity.UserInfo;
import com.example.storage.modules.sys.pojo.po.SelectApiKeyInfo;
import com.example.storage.modules.sys.pojo.vo.SelectByTemplateDto;
import com.example.storage.modules.sys.service.IApiKeyService;

@Service
public class ApiKeyServiceImpl implements IApiKeyService {
    @Autowired
    private ApiKeyInfoDao apiKeyInfoDao;
    
    @Autowired
    private ApiKeyPermissionDao apiKeyPermissionDao;

    @Override
    @Transactional
    public ApikeyInfo generate(UserInfo user, Date expire, String remarks) {
        ApikeyInfo apiKey = new ApikeyInfo();
        apiKey.setUuid(IdGen.uuid());
        apiKey.setUid(user.getId());
        apiKey.setCreateTime(new Date());
        apiKey.setExpire(expire);
        apiKey.setRemarks(remarks);
        apiKeyInfoDao.save(apiKey);
        return apiKey;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApikeyInfo> selectApiKeyListByUserId(String uid) {
        return apiKeyInfoDao.listByUid(uid);
    }

    @Override
    public void delete(String uuid) {
        apiKeyInfoDao.deleteByUidUuid(uuid);
    }

    @Override
    public void updata(ApikeyInfo apiKeyInfo) {
        apiKeyInfoDao.update(apiKeyInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public ApikeyInfo findByUuid(String apiKey) {
        return apiKeyInfoDao.findByUuid(apiKey);
    }

    @Override
    public Page<SelectApiKeyInfo> selectApiKeyInfoList(SelectByTemplateDto dto) {
        //根据是否有查询条件判断
        Page<SelectApiKeyInfo> page = new Page<>(dto.getPageIndex(), dto.getPageSize());
        List<SelectApiKeyInfo> list = apiKeyInfoDao.selectValidListByTemplate(page, StringUtils.trim(dto.getTemplate()), DELETE_STATE.NOT_DELETE.getCode());
        int count = apiKeyInfoDao.selectValidCountByTemplate(StringUtils.trim(dto.getTemplate()), DELETE_STATE.NOT_DELETE.getCode());
        page.setTotal(count);
        page.setRecords(list);
        return page;
	}

	@Override
	public void saveApiKey(String apikey, String permissions) {
		ApikeyPermission apiKey = apiKeyPermissionDao.validateApiKey(apikey);
		if (apiKey != null) {
			apiKeyPermissionDao.updateApiKey(apikey, permissions);
		} else {
			apiKeyPermissionDao.saveApiKey(apikey, permissions);
		}
	}
}
