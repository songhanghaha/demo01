package com.example.storage.modules.expresstake.pojo.po;

import lombok.Data;

@Data
public class ExpressuserPo {
	    /**
     *     ID
     */
    private String id;
    /**
     *     用户名称
     */
    private String name;
    /**
     *     用户类型
     */
    private String usertype;
    /**
     *     手机号码
     */
    private String tel;
    /**
     *     密码
     */
    private String pwd;
    /**
     *     性别 0 女 1男
     */
    private String gender;
    /**
     *     工作时间 0 上午 1 下午
     */
    private String worktime;
    /**
     *     微信号码
     */
    private String vchartno;
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

