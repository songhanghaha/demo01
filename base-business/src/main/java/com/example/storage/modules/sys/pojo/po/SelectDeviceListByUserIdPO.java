package com.example.storage.modules.sys.pojo.po;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 根据用户信息查询关联的终端列表
 */
@Data
@Accessors(chain = true)
public class SelectDeviceListByUserIdPO {

    /**
     * 设备ID
     */
    private String deviceId;

    /**
     * 设备名
     */
    private String deviceName;

    /**
     * 认证方式
     */
    private String authType;

}
