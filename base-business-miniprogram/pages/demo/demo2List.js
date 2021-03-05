var util = require("../../api/util.js")
Page({
/**
   * 页面的初始数据
   */
  data: {
    datass:[]
  },
  
  //加载数据列表
  loadAllData: function () {
    var that = this
    var url = 'manager/wxuser/getAllWxUser';
    var params1 = {
      id:wx.getStorageSync("uid")
    }
    console.log(params1);
    util.request(url, 'POST', params1, '正在加载数据', function (res) {
      console.log(res)
      that.setData({
        datass: res.data.data
      })
    })
  },
  mybindtap: function (event,url) {
    var that = this
    var itemId = event.currentTarget.id;
    console.log('提交数据：', itemId)
    var params1 = {
      "id": itemId
    }
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
      this.loadAllData();
    } else {
      wx.showToast({
        title: '操作失败！' + entity.code + "," + entity.message,
        icon: 'none'
      })
    }

  },


     //去添加
 bindToAdd: function() {
      wx.navigateTo({
        url: '../demo/demoAdd'
      })
  },
     //去修改
  bindToEdit: function(event) {
      var id = event.currentTarget.id;
      console.log("id:"+id)
      wx.navigateTo({
        url: '../demo/demoEdit?id='+id
      })
    },
   //去详情
  bindToDetail: function(event) {
    var id = event.currentTarget.id;
    console.log("id:"+id)
    wx.navigateTo({
      url: '../demo/demoDetail?id='+id
    })
  },

  //删除
  bindToDel: function(event) {
     this.mybindtap(event,"manager/wxuser/wxdel");
  },
  
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.loadAllData();
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.loadAllData();
  }

  

})



