package com.example.storage.modules.expresstake.pojo.po;

import lombok.Data;

@Data
public class ExpressinfPo {
	    /**
     *     ID
     */
    private String id;
    /**
     *     收件人姓名
     */
    private String receiver;
    /**
     *     收件地址
     */
    private String address;
    /**
     *     收件人手机号
     */
    private String receivetel;
    /**
     *     快递单号
     */
    private String expressno;
    /**
     *     楼层
     */
    private String floorr;
    /**
     *     快递员id
     */
    private String courierid;
    /**
     *     快递员姓名
     */
    private String couriername;
    /**
     *     快递员性别
     */
    private String couriergender;
    /**
     *     悬赏金额
     */
    private String price;
    /**
     *     取件人ID
     */
    private String collectid;
    /**
     *     取件人姓名
     */
    private String collectname;
    /**
     *     取件人手机号
     */
    private String collecttel;
    /**
     *     取件人楼层
     */
    private String collectfloor;
    /**
     *     取件人房间号
     */
    private String collectdoorno;
    /**
     *     取件费用
     */
    private String collectprice;
    /**
     *     取件时间
     */
    private String collecttime;
    /**
     *     用户ID
     */
    private String userid;
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

