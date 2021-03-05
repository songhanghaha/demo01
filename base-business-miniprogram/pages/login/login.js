// pages/wxlogin/wxlogin.js
var app = getApp()
var util = require("../../api/util.js")
var coon=require("../../api/Common.js")
Page({

  bindGetUserInfo: function (e) {
    var that = this;
    var wxu = e.detail.userInfo;
    if (wxu ===undefined){
      console.log("用户拒绝授权");
      // wx.navigateBack({
      //   delta: 1
      // })
      wx.redirectTo({
        url: '../account/account',
      })
    }else{
      //此处授权得到userInfo     
      console.log("微信用户信息=========" + wxu)
      //coon.userInfoSetInSQL(e.detail.userInfo)
      console.log("wxu:" + wxu.nickName);
      console.log("wxu:" + wxu.avatarUrl);
      console.log("wxu:" + wxu.gender);
      console.log("wxu-gender:" + wxu.province);
      console.log("wxu:" + wxu.city);
      console.log("wxu:" + wxu.country);
      wx.login({
        success: function (res) {
          //3登录成功请求获得openid
          var url = 'manager/wxuser/getSessionKey';
          var params1 = {
            "id": res.code
          }
          console.log("params1:"+params1);
          util.request(url, 'POST', params1, '正在加载数据', function (res) {
            console.log(res)
            var result=res.data;  
            var data = result.data;
            console.log("openid:" + data.openid)
            console.log("session_key:" + data.session_key)
            console.log("expires_in:" + data.expires_in)
            var params2 = {
            "id": data.openid
          }
          console.log("params2:"+params2);
          util.request('manager/wxuser/getuserByOpenid', 'POST', params2, '正在加载数据', function (res) {
              //
              var result2=res.data;
              var wxu=result2.data;
              //
              if(result2.data!=null){
                console.log(result2.code)
               //绑定过 ，直接返回      
                wx.setStorageSync('openid',result2.data.openid);         
                wx.setStorageSync("nickname", result2.data.nickname);
                wx.setStorageSync("uid",  result2.data.id);
                wx.setStorageSync("avatarurl", result2.data.avatarurl);
                wx.switchTab({
                  url: '../account/account'
                })
              }else{
                //没有绑定过，去绑定账号
                wx.redirectTo({
                  url: '../bind/bind?nickName=' + wxu.nickName + "&avatarUrl=" + wxu.avatarUrl + "&gender=" + wxu.gender + "&province=" + wxu.province + "&city=" + wxu.city + "&country=" + wxu.country + "&openid=" + data.openid + "&session_key=" + data.session_key + "&expires_in=" + data.expires_in
                })
              }
             
          })
           

          })


        }
      })

     
    }
   //用户信息
    // "wxUser.thirdSession": res.data,
    //   "wxUser.nickName": userInfo.nickName,
    //     "wxUser.avatarUrl": userInfo.avatarUrl,
    //       "wxUser.gender": userInfo.gender,
    //         "wxUser.provice": userInfo.provice,
    //           "wxUser.city": userInfo.city,
    //             "wxUser.country": userInfo.country

 
    //接下来写业务代码
    //最后，记得返回刚才的页面
    // wx.navigateBack({
    //   delta: 1
    // })
  },
  
  getPhoneNumber: function (e) {
    console.log("====errMsg:"+e.detail.errMsg)
    console.log("====iv:"+e.detail.iv)
    console.log("====encryptedData:"+e.detail.encryptedData)
    console.log("====认证情况："+e.detail.errMsg)
    var reg = RegExp(/getPhoneNumber:fail/);
    if (reg.exec(e.detail.errMsg)) {
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: e.detail.errMsg,
        success: function (res) { }
      })
    } else {
      wx.showModal({
        title: '提示',
        showCancel: false,
        content: '同意授权',
        success: function (res) { 
          console.log("=======同意授权=======================");
        }
      })
    }
  },

  getUserInfo:function(){
    console.log("=======getUserInfo")
  },

  toBind: function () {
    wx.redirectTo({
      url: '../bind/bind'
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    //coon.userLogin()
  },



  /**
   * 页面的初始数据
   */
  data: {

  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
    
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  }
})
