package com.example.storage.modules.wxuser.pojo.po;

import lombok.Data;

@Data
public class WxuserPo {
	    /**
     *     ID
     */
    private String id;
    /**
     *     账号
     */
    private String accountno;
    /**
     *     密码
     */
    private String pwd;
    /**
     *     微信openid
     */
    private String openid;
    /**
     *     用户头像
     */
    private String avatarurl;
    /**
     *     省
     */
    private String provice;
    /**
     *     市
     */
    private String city;
    /**
     *     区
     */
    private String country;
    /**
     *     gender
     */
    private String gender;
    /**
     *     昵称
     */
    private String nickname;
    /**
     *     三方session
     */
    private String thirdsession;
    /**
     *     创建时间
     */
    private java.util.Date createtime;
    /**
     *     修改时间
     */
    private java.util.Date updatetime;
    /**
     *     创建人
     */
    private String createby;
    /**
     *     更新人
     */
    private String updateby;
    /**
     *     删除标识
     */
    private String isdeleted;

}

