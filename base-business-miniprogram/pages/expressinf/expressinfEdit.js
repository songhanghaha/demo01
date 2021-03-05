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
  var url = 'manager/expressinf/api/detail';
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

     var receiver=e.detail.value.receiver;
     if(receiver==''){
       wx.showToast({
         title: '收件人姓名不能为空!',
         icon: 'none'
       })
       return 
     }
 var address=e.detail.value.address;
     if(address==''){
       wx.showToast({
         title: '收件地址不能为空!',
         icon: 'none'
       })
       return 
     }
 var receivetel=e.detail.value.receivetel;
     if(receivetel==''){
       wx.showToast({
         title: '收件人手机号不能为空!',
         icon: 'none'
       })
       return 
     }
 var expressno=e.detail.value.expressno;
     if(expressno==''){
       wx.showToast({
         title: '快递单号不能为空!',
         icon: 'none'
       })
       return 
     }
 var floorr=e.detail.value.floorr;
     if(floorr==''){
       wx.showToast({
         title: '楼层不能为空!',
         icon: 'none'
       })
       return 
     }
 var courierid=e.detail.value.courierid;
     if(courierid==''){
       wx.showToast({
         title: '快递员id不能为空!',
         icon: 'none'
       })
       return 
     }
 var couriername=e.detail.value.couriername;
     if(couriername==''){
       wx.showToast({
         title: '快递员姓名不能为空!',
         icon: 'none'
       })
       return 
     }
 var couriergender=e.detail.value.couriergender;
     if(couriergender==''){
       wx.showToast({
         title: '快递员性别不能为空!',
         icon: 'none'
       })
       return 
     }
 var price=e.detail.value.price;
     if(price==''){
       wx.showToast({
         title: '悬赏金额不能为空!',
         icon: 'none'
       })
       return 
     }
 var collectid=e.detail.value.collectid;
     if(collectid==''){
       wx.showToast({
         title: '取件人ID不能为空!',
         icon: 'none'
       })
       return 
     }
 var collectname=e.detail.value.collectname;
     if(collectname==''){
       wx.showToast({
         title: '取件人姓名不能为空!',
         icon: 'none'
       })
       return 
     }
 var collecttel=e.detail.value.collecttel;
     if(collecttel==''){
       wx.showToast({
         title: '取件人手机号不能为空!',
         icon: 'none'
       })
       return 
     }
 var collectfloor=e.detail.value.collectfloor;
     if(collectfloor==''){
       wx.showToast({
         title: '取件人楼层不能为空!',
         icon: 'none'
       })
       return 
     }
 var collectdoorno=e.detail.value.collectdoorno;
     if(collectdoorno==''){
       wx.showToast({
         title: '取件人房间号不能为空!',
         icon: 'none'
       })
       return 
     }
 var collectprice=e.detail.value.collectprice;
     if(collectprice==''){
       wx.showToast({
         title: '取件费用不能为空!',
         icon: 'none'
       })
       return 
     }
 var collecttime=e.detail.value.collecttime;
     if(collecttime==''){
       wx.showToast({
         title: '取件时间不能为空!',
         icon: 'none'
       })
       return 
     }
 var userid=e.detail.value.userid;
     if(userid==''){
       wx.showToast({
         title: '用户ID不能为空!',
         icon: 'none'
       })
       return 
     }

    
    var that =this
    var url = 'manager/expressinf/api/pdate';
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

