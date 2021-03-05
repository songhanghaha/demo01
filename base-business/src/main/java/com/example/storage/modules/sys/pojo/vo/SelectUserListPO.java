package com.example.storage.modules.sys.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户列表
 */
@Data
@Accessors(chain = true)
public class SelectUserListPO {

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户类型
     * 车主 授权 注册
     */
    private String userType;

    /**
     * 用户状态
     * 禁用  正常
     */
    private String status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
              * 用户是否是内测人员
     * 0.是 1.否
     */
    private String isTestUser;
}
