var util = require("../../api/util.js")
Page({
  /**
   * 页面的初始数据
   */
  data: {
  },
  //添加事项
  formSubmit: function (e) {
    console.log('form发生了submit事件，提交数据：', e.detail.value)
     //非空验证
     var name=e.detail.value.name;
     if(name==''){
       wx.showToast({
         title: '用户名称不能为空!',
         icon: 'none'
       })
       return 
     }
 var usertype=e.detail.value.usertype;
     if(usertype==''){
       wx.showToast({
         title: '用户类型不能为空!',
         icon: 'none'
       })
       return 
     }
 var tel=e.detail.value.tel;
     if(tel==''){
       wx.showToast({
         title: '手机号码不能为空!',
         icon: 'none'
       })
       return 
     }
 var pwd=e.detail.value.pwd;
     if(pwd==''){
       wx.showToast({
         title: '密码不能为空!',
         icon: 'none'
       })
       return 
     }
 var gender=e.detail.value.gender;
     if(gender==''){
       wx.showToast({
         title: '性别 0 女 1男不能为空!',
         icon: 'none'
       })
       return 
     }
 var worktime=e.detail.value.worktime;
     if(worktime==''){
       wx.showToast({
         title: '工作时间 0 上午 1 下午不能为空!',
         icon: 'none'
       })
       return 
     }
 var vchartno=e.detail.value.vchartno;
     if(vchartno==''){
       wx.showToast({
         title: '微信号码不能为空!',
         icon: 'none'
       })
       return 
     }

     
    var that =this
    var url = 'manager/expressuser/api/add';
    var params1 =e.detail.value;
    console.log(params1);
    util.request(url, 'POST', params1, '正在加载数据', function (res) {
      that.myresponse(res);
    })
  },

  //响应结果判断
  myresponse: function(res) {
    console.log(res)
    var entity = res.data;
    if ("10000" == entity.code) {     
      wx.showToast({
        title: '操作成功！',
        icon: 'success'
      })
    } else {
      wx.showToast({
        title: '操作失败！' + entity.code + "," + entity.message,
        icon: 'none'
      })
    }
  },

  formReset: function () {
    console.log('form发生了reset事件')
  },


  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {},

})

