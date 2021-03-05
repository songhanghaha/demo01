package com.example.storage.modules.expresstake.pojo.vo;

import lombok.Data;

import io.swagger.annotations.ApiModelProperty;

@Data
public class ExpressinfVo {
	    /**
     *     ID
     */
    @ApiModelProperty(value = "ID", name = "id")
    private String id;
    /**
     *     收件人姓名
     */
    @ApiModelProperty(value = "收件人姓名", name = "receiver")
    private String receiver;
    /**
     *     收件地址
     */
    @ApiModelProperty(value = "收件地址", name = "address")
    private String address;
    /**
     *     收件人手机号
     */
    @ApiModelProperty(value = "收件人手机号", name = "receivetel")
    private String receivetel;
    /**
     *     快递单号
     */
    @ApiModelProperty(value = "快递单号", name = "expressno")
    private String expressno;
    /**
     *     楼层
     */
    @ApiModelProperty(value = "楼层", name = "floorr")
    private String floorr;
    /**
     *     快递员id
     */
    @ApiModelProperty(value = "快递员id", name = "courierid")
    private String courierid;
    /**
     *     快递员姓名
     */
    @ApiModelProperty(value = "快递员姓名", name = "couriername")
    private String couriername;
    /**
     *     快递员性别
     */
    @ApiModelProperty(value = "快递员性别", name = "couriergender")
    private String couriergender;
    /**
     *     悬赏金额
     */
    @ApiModelProperty(value = "悬赏金额", name = "price")
    private String price;
    /**
     *     取件人ID
     */
    @ApiModelProperty(value = "取件人ID", name = "collectid")
    private String collectid;
    /**
     *     取件人姓名
     */
    @ApiModelProperty(value = "取件人姓名", name = "collectname")
    private String collectname;
    /**
     *     取件人手机号
     */
    @ApiModelProperty(value = "取件人手机号", name = "collecttel")
    private String collecttel;
    /**
     *     取件人楼层
     */
    @ApiModelProperty(value = "取件人楼层", name = "collectfloor")
    private String collectfloor;
    /**
     *     取件人房间号
     */
    @ApiModelProperty(value = "取件人房间号", name = "collectdoorno")
    private String collectdoorno;
    /**
     *     取件费用
     */
    @ApiModelProperty(value = "取件费用", name = "collectprice")
    private String collectprice;
    /**
     *     取件时间
     */
    @ApiModelProperty(value = "取件时间", name = "collecttime")
    private String collecttime;
    /**
     *     用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userid")
    private String userid;

}

