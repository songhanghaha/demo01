var util = require("../../api/util.js")
Page({
  /**
   * 页面的初始数据
   */
  data: {
    id:'',
    datass:''
  },
//加载数据列表
loadAllData: function () {
  var that = this
  var url = 'manager/wxuser/wxdetail';
  var params1 = {
    id:this.data.id
  }
  console.log(params1);
  util.request(url, 'POST', params1, '正在加载数据', function (res) {
    console.log(res)
    that.setData({
      datass: res.data.data
    })
  })
},

  //添加事项
  formSubmit: function (e) {
    console.log('form发生了submit事件，提交数据：', e.detail.value)

     var nickname=e.detail.value.nickname;
     var accountno=e.detail.value.accountno;
     console.log("nickname：",nickname)
     //非空验证
     if(nickname==''){
       wx.showToast({
         title: '昵称不能为空!',
         icon: 'none'
       })
       return 
     }
     if(accountno==''){
      wx.showToast({
        title: '账号不能为空!',
        icon: 'none'
      })
      return 
    }
    var that =this
    var url = 'manager/wxuser/wxupdate';
    var params1 =e.detail.value;
    console.log(params1);
    util.request(url, 'POST', params1, '正在加载数据', function (res) {
      that.myresponse(res);
    })
  },

  formReset: function () {
    console.log('edit form发生了reset事件')
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

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("detailID:"+options.id)
    this.setData({
      id:options.id
    })
    this.loadAllData();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.loadAllData();
  }

})