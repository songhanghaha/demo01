package com.example.storage.modules.expresstake.pojo.vo;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;

@Data
public class ExpressuserVo {
	    /**
     *     ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     *     用户名称
     */
    @ApiModelProperty(value = "用户名称", name = "name")
    private String name;
    /**
     *     用户类型
     */
    @ApiModelProperty(value = "用户类型", name = "usertype")
    private String usertype;
    /**
     *     手机号码
     */
    @ApiModelProperty(value = "手机号码", name = "tel")
    private String tel;
    /**
     *     密码
     */
    @ApiModelProperty(value = "密码", name = "pwd")
    private String pwd;
    /**
     *     性别 0 女 1男
     */
    @ApiModelProperty(value = "性别 0 女 1男", name = "gender")
    private String gender;
    /**
     *     工作时间 0 上午 1 下午
     */
    @ApiModelProperty(value = "工作时间 0 上午 1 下午", name = "worktime")
    private String worktime;
    /**
     *     微信号码
     */
    @ApiModelProperty(value = "微信号码", name = "vchartno")
    private String vchartno;

}

