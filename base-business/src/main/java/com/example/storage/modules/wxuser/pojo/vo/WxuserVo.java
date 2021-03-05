package com.example.storage.modules.wxuser.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WxuserVo {
	    /**
     *     ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     *     账号
     */
    @ApiModelProperty(value = "账号", name = "accountno")
    private String accountno;
    /**
     *     密码
     */
    @ApiModelProperty(value = "密码", name = "pwd")
    private String pwd;
    /**
     *     微信openid
     */
    @ApiModelProperty(value = "微信openid", name = "openid")
    private String openid;
    /**
     *     用户头像
     */
    @ApiModelProperty(value = "用户头像", name = "avatarurl")
    private String avatarurl;
    /**
     *     省
     */
    @ApiModelProperty(value = "省", name = "provice")
    private String provice;
    /**
     *     市
     */
    @ApiModelProperty(value = "市", name = "city")
    private String city;
    /**
     *     区
     */
    @ApiModelProperty(value = "区", name = "country")
    private String country;
    /**
     *     gender
     */
    @ApiModelProperty(value = "gender", name = "gender")
    private String gender;
    /**
     *     昵称
     */
    @ApiModelProperty(value = "昵称", name = "nickname")
    private String nickname;
    /**
     *     三方session
     */
    @ApiModelProperty(value = "三方session", name = "thirdsession")
    private String thirdsession;
    
    @ApiModelProperty(value = "code", name = "code")
    private String code;

}

