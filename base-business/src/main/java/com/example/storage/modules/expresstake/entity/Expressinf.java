package com.example.storage.modules.expresstake.entity;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 快递信息
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
@Data
@Accessors(chain = true)
@TableName(value="expresstake_expressinf")
public class Expressinf{
 
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
    public void setId(String id) {
     	this.id = id;
     }

     public String getId() { 
        return id;
     }

     public void setReceiver(String receiver) {
     	this.receiver = receiver;
     }

     public String getReceiver() { 
        return receiver;
     }

     public void setAddress(String address) {
     	this.address = address;
     }

     public String getAddress() { 
        return address;
     }

     public void setReceivetel(String receivetel) {
     	this.receivetel = receivetel;
     }

     public String getReceivetel() { 
        return receivetel;
     }

     public void setExpressno(String expressno) {
     	this.expressno = expressno;
     }

     public String getExpressno() { 
        return expressno;
     }

     public void setFloorr(String floorr) {
     	this.floorr = floorr;
     }

     public String getFloorr() { 
        return floorr;
     }

     public void setCourierid(String courierid) {
     	this.courierid = courierid;
     }

     public String getCourierid() { 
        return courierid;
     }

     public void setCouriername(String couriername) {
     	this.couriername = couriername;
     }

     public String getCouriername() { 
        return couriername;
     }

     public void setCouriergender(String couriergender) {
     	this.couriergender = couriergender;
     }

     public String getCouriergender() { 
        return couriergender;
     }

     public void setPrice(String price) {
     	this.price = price;
     }

     public String getPrice() { 
        return price;
     }

     public void setCollectid(String collectid) {
     	this.collectid = collectid;
     }

     public String getCollectid() { 
        return collectid;
     }

     public void setCollectname(String collectname) {
     	this.collectname = collectname;
     }

     public String getCollectname() { 
        return collectname;
     }

     public void setCollecttel(String collecttel) {
     	this.collecttel = collecttel;
     }

     public String getCollecttel() { 
        return collecttel;
     }

     public void setCollectfloor(String collectfloor) {
     	this.collectfloor = collectfloor;
     }

     public String getCollectfloor() { 
        return collectfloor;
     }

     public void setCollectdoorno(String collectdoorno) {
     	this.collectdoorno = collectdoorno;
     }

     public String getCollectdoorno() { 
        return collectdoorno;
     }

     public void setCollectprice(String collectprice) {
     	this.collectprice = collectprice;
     }

     public String getCollectprice() { 
        return collectprice;
     }

     public void setCollecttime(String collecttime) {
     	this.collecttime = collecttime;
     }

     public String getCollecttime() { 
        return collecttime;
     }

     public void setUserid(String userid) {
     	this.userid = userid;
     }

     public String getUserid() { 
        return userid;
     }

     public void setCreatetime(java.util.Date createtime) {
     	this.createtime = createtime;
     }

     public java.util.Date getCreatetime() { 
        return createtime;
     }

     public void setUpdatetime(java.util.Date updatetime) {
     	this.updatetime = updatetime;
     }

     public java.util.Date getUpdatetime() { 
        return updatetime;
     }

     public void setCreateby(String createby) {
     	this.createby = createby;
     }

     public String getCreateby() { 
        return createby;
     }

     public void setUpdateby(String updateby) {
     	this.updateby = updateby;
     }

     public String getUpdateby() { 
        return updateby;
     }

     public void setIsdeleted(String isdeleted) {
     	this.isdeleted = isdeleted;
     }

     public String getIsdeleted() { 
        return isdeleted;
     }

 
}

