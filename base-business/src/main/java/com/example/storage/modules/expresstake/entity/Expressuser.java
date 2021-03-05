package com.example.storage.modules.expresstake.entity;
import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 用户信息
 * @author frank
 * @datatime 2021-01-11 03:57:49
 */
@Data
@Accessors(chain = true)
@TableName(value="expresstake_expressuser")
public class Expressuser{
 
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
    public void setId(String id) {
     	this.id = id;
     }

     public String getId() { 
        return id;
     }

     public void setName(String name) {
     	this.name = name;
     }

     public String getName() { 
        return name;
     }

     public void setUsertype(String usertype) {
     	this.usertype = usertype;
     }

     public String getUsertype() { 
        return usertype;
     }

     public void setTel(String tel) {
     	this.tel = tel;
     }

     public String getTel() { 
        return tel;
     }

     public void setPwd(String pwd) {
     	this.pwd = pwd;
     }

     public String getPwd() { 
        return pwd;
     }

     public void setGender(String gender) {
     	this.gender = gender;
     }

     public String getGender() { 
        return gender;
     }

     public void setWorktime(String worktime) {
     	this.worktime = worktime;
     }

     public String getWorktime() { 
        return worktime;
     }

     public void setVchartno(String vchartno) {
     	this.vchartno = vchartno;
     }

     public String getVchartno() { 
        return vchartno;
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

