//pages/utils/util 
var app = getApp(); 
var apiUrl = app.globalData.apiUrl;

function request(url, method, data, message, success, fail) {
  wx.showNavigationBarLoading()
  if (message != "") {
    wx.showLoading({
      title: message,
    })
  }
  wx.request({
    url: apiUrl+url,
    data: data,
    method: method,
    success: function (res) {
      wx.hideNavigationBarLoading()
      if (message != "") {
        wx.hideLoading()
      }
      if (res.data.code == '10000') {
        success(res)
      } else {
        console.log("请求成功，返回信息：" + res.statusCode)
      }
    },
    fail: function (err) {
      wx.hideNavigationBarLoading()
      if (message != "") {
        wx.hideLoading()
      }
      console.log('请求失败：' + err)
    },
  })
}
function beyond_kucun() {
  wx.showModal({
    title: '错误提示',
    content: '操作失败,请检查您输入的数量和该商品的库存数',
  })
}
module.exports = {
  request: request,
  apiUrl: apiUrl,
  beyond_kucun: beyond_kucun
}  