Page({
  data: {
    imgUrls: [
      '../../images/hdp_1.jpg',
      '../../images/hdp_2.jpg',
      '../../images/hdp_3.jpg',
    ],
    indicatorDots: true,
    autoplay: true,
    interval: 3000,
    duration: 500
  },
   //事件处理函数
  bindTap: function() {
    wx.switchTab({
      url: '../demo/demoList',
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }

 

})
