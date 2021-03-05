package com.example.storage.modules.sys.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * kss manager 查询ApiKey
 */
@Data
@Accessors(chain = true)
public class SelectApiKeyInfo {

    /**
     * ApiKey
     */
    private String uuid;

    /**
     * 过期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expire;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 说明
     */
    private String remarks;

}
