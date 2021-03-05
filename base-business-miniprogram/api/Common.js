var app=getApp()
//用户登录
function userLogin(){
  wx.checkSession({
    success:function(){
      //存在登录状态
      console.log("=========存在登录状态")
    },
    fail:function(){
      //不存在登录状态
      onLogin();
    }
  })
}

function onLogin(){
  wx.login({
    //登录成功
    success:function(res){
     if(res.code){
        //发起网络请求
        wx.request({
          url: app.globalData.apiUrl+'/gateway/getSessionKey.action',
          data:{
            code:res.code
          },
          success:function(res){
            const self=this
            var flag=true;
            var json=res.data;
           // var json = JSON.parse(res)
            console.log("============dsfjsdkfjdskfdskfsdf========"+json)
            if ("ACK" == json.errorCode){//逻辑成功
              console.log(res.data)
              //获取到用户凭证，存储3rd_session            
              wx.setStorage({
                key: 'third_Session',
                data: json.result.session_key
              })
              getUserInfo()
            }else{
              console.log("FAI=================")
            }
          },
          fail:function(res){
            
            console.log("fail=======" + res.errMsg)
          }
        })
     }
    },
    //登录失败
    fail:function(res){
     console.log("fail=========="+res)
    }
  })
}
//获得微信用户，成功后保存数据到后台
function getUserInfo(){
  console.log("======调用getUserInfo")
  wx.getUserInfo({
    withCredentials: true,
    success:function(res){
      /**
       * 如果用户授权过了，这里可以拿到用户的信息
       * 可以做相关的业务
       */
      var userInfo=res.userInfo
      console.log("getUserInfo============="+res)
      //userInfoSetInSQL(userInfo)
    },
    fail:function(res){
      //res.errMsg=getUserInfo:fail scope unauthorized
      console.log(res)
      //获取用户信息失败后。请跳转授权页面
      wx.showModal({
        title:  '警告',
        content:  '尚未进行授权，请点击确定跳转到授权页面进行授权。',
        success:  function  (res)  {
          if (res.confirm)  {
            console.log('用户点击确定')
            wx.navigateTo({
              url: '../auth/auth'
            })
          }     
        }
     })
    }
  })
}

//保存用户数据到后台
function userInfoSetInSQL(userInfo){
  console.log("===========保存用户数据")
 wx.getStorage({
   key: 'third_Session',
   success: function(res) {
     wx.request({
       url: app.globalData.apiUrl +'/gateway/saveWeixinMiniUser.action',
       data:{
         "wxUser.thirdSession":res.data,
         "wxUser.nickName":userInfo.nickName,
         "wxUser.avatarUrl":userInfo.avatarUrl,
         "wxUser.gender":userInfo.gender,
         "wxUser.provice":userInfo.provice,
         "wxUser.city":userInfo.city,
         "wxUser.country":userInfo.country
       },
       success:function(res){
         var json = JSON.parse(res)
         if ("ACK" == json.errorCode) {
           //SQL更新用户数据成功
           console.log('保存成功')
         }else{
           //SQL更新用户数据失败
           console.log('保存失败')
         }
       }
     })
   },
 })
}
module.exports = {
  apiUrl: app.globalData.apiUrl,//全局访问地址
  userLogin: userLogin,//获得用户手机号
  userInfoSetInSQL: userInfoSetInSQL//保存用户信息
}