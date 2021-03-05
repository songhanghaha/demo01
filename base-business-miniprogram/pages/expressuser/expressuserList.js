var util = require("../../api/util.js")
var app = getApp()
Page({
  data: {
     /** 
        * 页面配置 
        */
       winWidth: 0,
       winHeight: 0,
   
    // tab切换  
    currentTab: 0,
    //列表data
    datass:[],
    datass2:[],
    datass3:[]
  },
 
  /** 
     * 滑动切换tab 
     */
  bindChange: function (e) {
    var that = this;
    that.setData({ currentTab: e.detail.current });

  },
  /** 
   * 点击tab切换 
   */
  swichNav: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    } else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
  },



  //加载数据列表
  loadAllData: function () {
    var that = this
    var url = 'manager/expressuser/api/getAllExpressuser';
    var params1 = {
      id:wx.getStorageSync("uid")
    }
    
    util.request(url, 'POST', params1, '正在加载数据', function (res) {
      console.log(res)
      that.setData({
        datass: res.data.data
      })
      that.setData({
        datass2: res.data.data
      })
      that.setData({
        datass3: res.data.data
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
        url: '../expressuser/expressuserAdd'
      })
  },
     //去修改
  bindToEdit: function(event) {
      var id = event.currentTarget.id;
      console.log("id:"+id)
      wx.navigateTo({
        url: '../expressuser/expressuserEdit?id='+id
      })
    },
   //去详情
  bindToDetail: function(event) {
    var id = event.currentTarget.id;
    console.log("id:"+id)
    wx.navigateTo({
      url: '../expressuser/expressuserDetail?id='+id
    })
  },

  //删除
  bindToDel: function(event) {
     this.mybindtap(event,"manager/expressuser/api/del");
  },
   
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.loadAllData();
    var that = this;
    /** 
     * 获取系统信息 
     */
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          winWidth: res.windowWidth,
          winHeight: 1000//res.windowHeight
        });
      }

    });

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.loadAllData();
  }
})  

