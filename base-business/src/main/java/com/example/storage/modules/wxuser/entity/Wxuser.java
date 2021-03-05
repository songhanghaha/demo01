package com.example.storage.modules.wxuser.entity;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 微信用户
 * @author frank
 * @datatime 2020-11-15 09:13:17
 */
@Data
@Accessors(chain = true)
@TableName(value="wxuser")
public class Wxuser{
 
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
    public void setId(String id) {
     	this.id = id;
     }

     public String getId() { 
        return id;
     }

     public void setAccountno(String accountno) {
     	this.accountno = accountno;
     }

     public String getAccountno() { 
        return accountno;
     }

     public void setPwd(String pwd) {
     	this.pwd = pwd;
     }

     public String getPwd() { 
        return pwd;
     }

     public void setOpenid(String openid) {
     	this.openid = openid;
     }

     public String getOpenid() { 
        return openid;
     }

     public void setAvatarurl(String avatarurl) {
     	this.avatarurl = avatarurl;
     }

     public String getAvatarurl() { 
        return avatarurl;
     }

     public void setProvice(String provice) {
     	this.provice = provice;
     }

     public String getProvice() { 
        return provice;
     }

     public void setCity(String city) {
     	this.city = city;
     }

     public String getCity() { 
        return city;
     }

     public void setCountry(String country) {
     	this.country = country;
     }

     public String getCountry() { 
        return country;
     }

     public void setGender(String gender) {
     	this.gender = gender;
     }

     public String getGender() { 
        return gender;
     }

     public void setNickname(String nickname) {
     	this.nickname = nickname;
     }

     public String getNickname() { 
        return nickname;
     }

     public void setThirdsession(String thirdsession) {
     	this.thirdsession = thirdsession;
     }

     public String getThirdsession() { 
        return thirdsession;
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

