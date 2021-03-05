package com.example.storage.modules.sys.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * tsp manager 管理员列表
 */
@Data
@Accessors(chain = true)
public class SelectManagerUserInfo {

    private String id;

    private String email;

    private String username;

    private String mobile;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
