package com.example.storage.modules.sys.entity;
import lombok.Data;

import java.util.Date;

@Data
public class Role {

    private String id;

    private String roleName;

    private String chName;

    private String createBy;

    private Date createDate;

    private String updateBy;

    private Date updateDate;

    private String remarks;

    private Integer delFlag;
    
    //add
    private  String permissions;

}
