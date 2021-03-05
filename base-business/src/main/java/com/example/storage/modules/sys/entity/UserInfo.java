package com.example.storage.modules.sys.entity;
import java.util.Date;
import java.util.List;

import com.example.storage.common.base.BaseEntity;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
public class UserInfo extends BaseEntity{

    private String username;

    private String password;

    private String email;

    private String company;

    private String location;

    private String position;

    private String mobile;

    private Integer state = 0;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String remarks;

    private Integer delFlag;
    
   // private List<Role> roleList = Lists.newArrayList(); // 拥有角色列表
    
    //-----------------------
    private String roles;


}
