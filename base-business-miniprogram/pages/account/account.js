Page({
  data: {
   nickname:"",
   avatarurl: "",
   flag:false
  },
  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    this.setData({
      nickname: wx.getStorageSync("nickname"),
      avatarurl: wx.getStorageSync("avatarurl")
    })
  },
  /**
  * 生命周期函数--监听页面显示
  */
  onShow: function () {
    this.setData({
      nickname: wx.getStorageSync("nickname"),
      avatarurl: wx.getStorageSync("avatarurl")
    })
  },
   //去登录
   bindTap: function() {
     wx.navigateTo({
       url: '../login/login',
     })
  }

})