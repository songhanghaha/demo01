package com.example.storage.modules.sys.entity;


import java.util.Date;

import lombok.Data;

@Data
public class MailValidate {

    private String id;

    private String targetMail;

    private String expiryTime;

    private String token;

    private String createBy;

    private Integer state = 0;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String remarks;

    private Integer delFlag;
}
