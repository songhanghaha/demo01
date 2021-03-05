// pages/login/login.js
var util = require("../../api/util.js")
var app=getApp()
Page({
  /**
  * 页面的初始数据
  */
  data: {
    tel:"",
    smscode:"",
    
    //
    nickName:"",
    
    avatarUrl: "",
    nickName: "",
    gender: "",
    province: "",
    city: "",
    country: "",

    openid:"",
    session_key:"",
    expires_in:""
  },

  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    var wxu=options;
    this.setData({
      nickName: wxu.nickName,
      avatarUrl: wxu.avatarUrl,
      gender: wxu.gender,
      province: wxu.province,
      city: wxu.city,
      country: wxu.country,
      openid: wxu.openid ,
      session_key: wxu.session_key,
      expires_in: wxu.expires_in
    })
    console.log("wxu:" + wxu.nickName);
    console.log("wxu:" + wxu.avatarUrl);
    console.log("wxu-gender:" + wxu.gender); 
    console.log("wxu-porvince:" + wxu.porvince);
    console.log("wxu:" + wxu.city);
    console.log("wxu:" + wxu.country);
    console.log("wxu:" + wxu.openId);
    console.log("wxu:" + wxu.country);
    console.log("wxu:" + wxu.openId);
  },

  //绑定用户
  formSubmit: function (e) {
    var account = e.detail.value.account
    var pwd = e.detail.value.pwd
    var repwd = e.detail.value.repwd
    if (account == undefined || '' == account){
        wx.showToast({
          title: "请输入账号",
          icon: 'none',
          duration: 2000,
        })
       return;
    }
    if (pwd == undefined || '' == pwd) {
      wx.showToast({
        title: "请输入密码",
        icon: 'none',
        duration: 2000,
      })
      return;
    }
    if (repwd == undefined || '' == repwd) {
      wx.showToast({
        title: "请输入确认密码",
        icon: 'none',
        duration: 2000,
      })
      return;
    }

    if (repwd != pwd) {
      wx.showToast({
        title: "两次输入密码不一致！",
        icon: 'none',
        duration: 2000,
      })
      return;
    }
    var that = this
    var url = 'manager/wxuser/binWxUser';
    var params1 = {
      nickname: this.data.nickName,
      avatarurl: this.data.avatarUrl,
      gender: this.data.gender,
      provice: this.data.province,
      city: this.data.city,
      country: this.data.country,
      accountno:account,
      pwd:pwd,
      openid: this.data.openid,
      thirdsession: this.data.session_key,
      expires_in: this.data.expires_in
    }
    console.log(params1);
    util.request(url, 'POST', params1, '正在加载数据', function (res) {
         console.log(res)
        var entity = res.data;
        if ("10000" == entity.code) {         
          wx.showToast({
            title: '操作成功！',
            icon: 'success'
          })
          wx.setStorageSync("nickname", that.data.nickName);
          wx.setStorageSync("uid", entity.data);
          wx.setStorageSync("avatarurl", that.data.avatarUrl);
          wx.switchTab({
            url: '../account/accountIndex'
          })
        } else {
          wx.showToast({
            title: '操作失败！' + entity.code + "," + entity.message,
            icon: 'none'
          })
        }
    })
  //   if (telflag){
  //     if (smscodeflag){
  //       wx.request({
  //         url: app.globalData.apiUrl + '/gateway/checkValiateCode.action',
  //         data: {
  //           mobile: e.detail.value.tel,
  //           validateCode: e.detail.value.smscode
  //         },
  //         success: function (res) {
  //           console.log(res.data)
  //           var json = res.data
  //           if ("ACK" == json.errorCode) {
  //             console.log("======登录成功" + json.errorCode)
  //             wx.showToast({
  //               title: "登陆成功",
  //               icon: 'success',
  //               duration: 2000,
  //               success: function () {
  //                 setTimeout(function () {
  //                   wx.switchTab({
  //                     url: '../jiedan/jiedan',
  //                   })
  //                 }, 2000)
  //               }
  //             })
  //           } else {
  //             //SQL更新用户数据失败
  //             console.log('登录失败')
  //             wx.showToast({
  //               title: json.errorMsg,
  //               icon: 'none',
  //               duration: 2000,
  //             })
  //           }
  //         }
  //       })
  //     }else{
  //       wx.showToast({
  //         title: "请输入验证码",
  //         icon: 'none',
  //         duration: 2000,
  //       })
  //     }     
  //  }else{
  //     wx.showToast({
  //       title: "请输入手机号",
  //       icon: 'none',
  //       duration: 2000,
  //     })
  //  }
  }


})
