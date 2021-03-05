var util = require("../../api/util.js")
var app = getApp()
Page({
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

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("detailID:"+options.id)
    this.setData({
      id:options.id
    })
  },
  
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.loadAllData();
  }
})  