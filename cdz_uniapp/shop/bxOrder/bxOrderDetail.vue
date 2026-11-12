<template>
	<view class="" style="padding-bottom: 80px;">
		<uv-skeleton title :loading="loading" rows="6" rowsHeight="120" rowsWidth="100%">
			<!-- #ifdef H5 -->
			<view class="box" style="margin-top: 10rpx;">
			<!-- #endif -->
				<!-- #ifndef H5 -->
				<view class="box">
				<!-- #endif -->
					<view class="titl" v-if="order.status==1">待支付...</view>
					<view class="titl" v-if="order.status==2">待使用...</view>
					<view class="titl" v-if="order.status==3">使用中...</view>
					<view class="titl" v-if="order.status==4">已完成...</view>
					<view class="titl" v-if="order.status==5">已取消...</view>
					<view class="titl" v-if="order.status==6">待评价...</view>
					<view class="text-sm margin-top-xs" style="color: #999999;" v-if="order.status==3">订单预订成功，祝你使用愉快
					</view>
				</view>
				<view class="box ">
					<view class="flex align-center justify-between">
						<view class="towbox" style="width: 80%;">

							<image
								:src="order.tearoom && order.tearoom.tearoomTitleImage?order.tearoom.tearoomTitleImage:'../../static/logo.png'">
							</image>
							<view class="margin-left-sm" style="width:75%;">
								<view class="tit">{{order.goods.goodsName}}</view>
								<view class="price">
									{{order.status==1 || order.status == 5 ?'应付':'实付'}}:￥<text>{{order.onePayMoney?order.onePayMoney:0}}</text>
								</view>
							</view>
						</view>
						<!-- <view class="btnn" v-if="order.status==3" @click="showHour = true">加时</view> -->
					</view>
					<view class=" flex align-center justify-between margin-tb">
						<view>开始时间</view>
						<view style="color: #666666;">{{order.startTime}}</view>
					</view>
					<view class=" flex align-center justify-between ">
						<view>结束时间</view>
						<view style="color: #666666;">{{order.endTime?order.endTime:'----'}}</view>
					</view>
				</view>
				<view class="box " v-if="order.userName">
					<view class="flex">
						<view class="titl">{{order.userName}}</view>
						<view class="text-sm margin-left-sm margin-top-xs" style="color: #999999;">{{order.phone}}
						</view>
					</view>
					<view class="line"></view>
					<view class="text-center text-24 text-bold" @click="bindPhone(order.phone)">联系客人</view>
				</view>
				<!-- <view class="box  flex align-center justify-between" @click="goswitch()">
					<view class="flex align-center justify-between">
						<image src="../static/kg.png" style="width:55rpx;height:61rpx;"></image>
						<view class="margin-left-xs">
							<view class="titl">开关操作</view>
							<view class="text-sm" style="color: #999999;">点击进入开关启动</view>
						</view>
					</view>
					<image src="../../static/images/my/right.png" style="width: 16rpx;height: 28rpx;"></image>
				</view> -->

				<!-- <view class="box " v-if="order.tearoom_info">
				<view class="titl margin-bottom">茶室信息<text
						style="color:red;font-size:2px;margin-left: 10rpx;">提前{{hour}}分钟展示</text></view>
				<view class="flex align-center justify-between margin-bottom">
					<view class="">开门密码</view>
					<view class="" style="color: #999999;">{{order.door_pwd?order.door_pwd:''}}</view>
				</view>
				<view class="flex align-center justify-between margin-bottom">
					<view class="">WIFI密码</view>
					<view v-if="order.wifi_pwd" @click="copyWifi(order.wifi_pwd)" class="flex align-center"
						style="color: #999999;">
						{{order.wifi_pwd}}
						<u-icon name="order" color="#999999" size="20"></u-icon>
					</view>
				</view>
				<view class="flex align-center justify-between ">
					<view class="">房间号码</view>
					<view class="" style="color: #999999;">{{order.room_num?order.room_num:''}}</view>
				</view>
			</view> -->
				<!-- <view class="box ">
				<view class="titl margin-bottom">个人信息</view>
				<view class="flex align-center justify-between margin-bottom">
					<view class="">姓名</view>
					<view class="" style="color: #999999;">{{order.order_name}}</view>
				</view>
				<view class="flex align-center justify-between ">
					<view class="">联系方式</view>
					<view class="" style="color: #999999;">{{order.order_phone}}</view>
				</view>
			</view> -->
				<!-- <view class="box ">
				<view class="titl margin-bottom">温馨提示</view>
				<view class="flex align-center justify-between margin-bottom">
					<view class="">有效期</view>
					<view class="" style="color: #999999;">{{order.expired_time}}</view>
				</view>
				<view class="flex align-center justify-between margin-bottom" v-if="order.store_info">
					<view class="">使用时间</view>
					<view class="" style="color: #999999;">{{order.store_info.store_time}}</view>
				</view>
				<view class="flex align-center justify-between ">
					<view class="">使用规则</view>
					<view class="" style="color: #999999;">仅限购买一次 多次无效</view>
				</view>
			</view> -->
				<view class="box ">
					<view class="titl margin-bottom">订单信息</view>
					<!-- <view class="flex align-center justify-between margin-bottom"
						v-if="order.addHour && order.addHour != 0">
						<view class="">加时</view>
						<view class="" style="color: #999999;">{{order.addHour}}小时</view>
					</view> -->
					<!-- <view class="flex align-center justify-between margin-bottom"
						v-if="order.addPayMoney && order.addPayMoney != 0">
						<view class="">加时金额</view>
						<view class="" style="color: #999999;">￥{{order.addPayMoney}}</view>
					</view> -->
					<view class="flex align-center justify-between margin-bottom"
						v-if="order.couponMoney && order.couponMoney != 0">
						<view class="">优惠劵</view>
						<view class="" style="color: #999999;">-￥{{order.couponMoney}}</view>
					</view>
					<view v-if="order.payMoney && order.payMoney>0"
						class="flex align-center justify-between margin-bottom">
						<view class="">商品总价</view>
						<view class="" style="color: #999999;">￥{{order.payMoney?order.payMoney:0}}</view>
					</view>
					<view v-if="order.payWay" class="flex align-center justify-between margin-bottom">
						<view class="">支付方式</view>
						<view class="" style="color: #999999;" v-if="order.payWay==3">会员卡支付</view>
						<view class="" style="color: #999999;" v-if="order.payWay==2">支付宝支付</view>
						<view class="" style="color: #999999;" v-if="order.payWay==1">微信支付</view>
					</view>
					<view class="flex align-center justify-between margin-bottom">
						<view class="">订单编号</view>
						<view class="flex align-center" style="color: #999999;">
							{{order.ordersNo}}
							<u-icon name="file-text" @click="copyOrder(order.ordersNo)" color="#999999"
								size="24"></u-icon>
						</view>
					</view>
					<view class="flex align-center justify-between margin-bottom">
						<view class="">下单时间</view>
						<view class="" style="color: #999999;">{{order.createTime}}</view>
					</view>
					<view class="flex align-center justify-between " v-if="order.remark">
						<view class="">下单备注</view>
						<view class="" style="color: #999999;">{{order.remark}}</view>
					</view>
				</view>

				<view class="taber" v-if="order.status==1||order.status==2">
					<view class="btn" @click="bindorderOff">取消订单</view>
					<!-- <view class="btnn" @click="goswitch()">立即开门</view> -->
					<!-- <view class="btn" v-if="order.status==5" @click="bindorderdete">删除订单</view>
				<view class="btnn" @click="showpay = true" v-if="order.status==1">立即支付</view>
				<view class="btn" v-if="order.status==3" @click="hujiao" @>呼叫保洁</view>
				<view class="btnn" @click="showHour = true" v-if="order.status==3">加时</view> -->
				</view>
		</uv-skeleton>
	</view>
</template>

<script>
	import moment from 'moment'
	export default {
		data() {
			return {
				loading: true,
				openWay: 0,
				openLists: [],
				showpay: false,
				ordersNo: '',
				order: [],
				hour: 0,
				showHour: false,
				enddata: '',
				timePrice: 0,
				timeShow: false,
				columns: []
			}
		},
		onLoad(option) {

			if (option.ordersNo) {
				this.ordersNo = option.ordersNo
				this.getDetail()
			}
		},
		methods: {
			//复制wifi密码
			copyWifi(val) {
				uni.setClipboardData({
					data: val,
					success() {
						uni.showToast({
							title: 'WIFI密码已复制'
						})
					}
				})
			},
			//呼叫保洁
			hujiao() {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认呼叫保洁吗？',
					confirmText: '确认',
					complete(ret) {
						if (ret.confirm) {
							let data = {
								token: uni.getStorageSync('token'),
								order_id: that.orderId
							}
							that.$Request.postT('/api/index/order/call_by_cleaner', data).then(res => {
								if (res.code == 1) {
									uni.showToast({
										title: '已呼叫'
									})
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})

			},
			closePicker() {
				this.timeShow = false
			},
			//复制订单号
			copyOrder(order) {
				uni.setClipboardData({
					data: order,
					success() {
						uni.showToast({
							title: '已复制订单号'
						})
					},
					fail() {
						uni.showToast({
							title: '复制失败，请重试！',
							icon: 'none'
						})
					}
				})
			},
			confirm(e) {
				console.log(e)
				let endTime = e.value[0]
				this.enddata = moment().format('YYYY-MM-DD') + ' ' + e.value[0]
				// console.log(this.enddata, moment().format('YYYY-MM-DD'))
				// this.time = data
				const Time = moment(this.enddata).diff(moment(this.order.end_time), 'hours')
				this.timePrice = parseFloat(this.order.goods_info.goods_price) * parseFloat(Time).toFixed(2)
				// console.log(Time,'8888',this.timePrice)
				this.timeShow = false
			},
			// 根据日期获取时间
			getDataTime(index) {
				let data = {
					goods_id: this.order.goods_info.id,
					year_time: '',
				}
				this.$Request.postT('api/index/order/time_manage', data).then(res => {
					if (res.code == 1) {
						let arr = []
						let arrs = res.data
						res.data.some((item, index) => {
							// item.isSelect = false
							if (item.is_busy == 0) {
								//判断下一条是否是可选的
								if ((index + 1) < res.data.length && res.data[index + 1].is_busy ==
									1) { //否
									arr.push(item.end_time)
									return true
								} else { //不是
									arr.push(item.end_time)
								}

							}
						})

						// arr.splice(0, 1)
						this.columns.push(arr)
						console.log(this.columns, '11111')
						// this.hostArr = res.data
						// if (index != '-1') this.day_index = index
						// let time = {
						// 	hostArr:this.hostArr,
						// 	dayArr:this.dayArr,
						// 	yearTime:this.yearTime
						// }
						// this.$emit('submitData', time)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			closeHour() {
				this.showHour = false
				this.enddata = ''
				this.timePrice = 0
			},
			//预约时间
			godata() {
				if (this.columns.length == 0 || this.columns[0].length == 0) {
					uni.showToast({
						title: '暂无可选择的时间',
						icon: 'none'
					})
				} else {
					this.timeShow = true
				}

				// uni.navigateTo({
				// 	url: '/pages/index/data?goodsId=' + this.order.goods_id
				// })
			},
			selectWay: function(id) {
				this.openWay = id;
			},
			goswitch() {
				uni.navigateTo({
					url: '/my/order/switching?goodsId=' + this.order.goodsId
				})
				// uni.navigateTo({
				// 	url: '/my/setting/switching?goods_id='+this.order.goods_id
				// })
			},
			// 一键导航
			openMap(latitude, longitude, name) {
				uni.openLocation({
					latitude: latitude - 0, //要去的纬度-地址       
					longitude: longitude - 0, //要去的经度-地址
					name: name, //地址名称
					address: name, //详细地址名称
					success: function() {
						console.log('导航成功');
					},
					fail: function(error) {
						console.log(error)
					}
				});
			},
			bindPhone(phone) {
				uni.makePhoneCall({
					phoneNumber: phone //仅为示例
				});
			},
			//订单详情
			getDetail() {
				let data = {
					ordersNo: this.ordersNo
				}
				this.$Request.getT('/app/orders/selectOrdersDetails', data).then(res => {
					if (res.code === 0) {
						this.order = res.data
						if (this.order.goodsCoverImage) {
							this.order.goodsCoverImage = this.order.goodsCoverImage.split(',')
						} else {
							this.order.goodsCoverImage = []
						}
						// this.getDataTime()
						// if (this.order.moneyJson) {
						// 	let moneyJson = JSON.parse(this.order.moneyJson)
						// 	this.order.moneyJson = moneyJson
						// }

						// if (this.order.riderPhone) {
						// 	let mobile = this.order.riderPhone
						// 	this.order.riderPhones = mobile.substring(0, 3) + "****" + mobile.substring(7)
						// }
						// if (this.order.str_time && this.order.end_time) {
						// 	let year = this.order.str_time.substring(0, 10)
						// 	let strTime = this.order.str_time.substring(10, 17)
						// 	let endTime = this.order.end_time.substring(10, 17)
						// 	// console.log(year, '-----', strTime, endTime)
						// 	this.order.data = year
						// 	this.order.time = strTime + '~' + endTime
						// }
					}
					uni.hideLoading()
					this.loading = false
				});
			},
			// 取消订单
			bindorderOff() {
				let that = this
				uni.showModal({
					title: '温馨提示',
					content: '确定取消订单?',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认',
					success: res => {
						if (res.confirm) {
							let data = {
								ordersNo: that.ordersNo
							}
							that.$Request.postT('/app/orders/cancelOrders', data)
								.then(res => {
									if (res.code == 0) {
										uni.showToast({
											title: '订单取消成功'
										});
										that.getDetail()
										setTimeout(function() {
											uni.navigateBack()
										}, 100)
									} else {
										uni.hideLoading();
										uni.showModal({
											showCancel: false,
											title: '订单失败',
											content: res.msg
										});
									}
								});
						}
					}
				});
			},
			// 删除订单
			bindorderdete() {
				let that = this
				uni.showModal({
					title: '温馨提示',
					content: '确定删除订单?',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认',
					success: res => {
						if (res.confirm) {
							that.$Request.postT('/api/index/order/user_del_order?order_id=' + that.orderId)
								.then(res => {
									if (res.code == 1) {
										uni.showToast({
											title: '订单删除成功'
										});
										that.getDetail()
										setTimeout(function() {
											uni.navigateBack()
										}, 100)
									} else {
										uni.hideLoading();
										uni.showModal({
											showCancel: false,
											title: '订单失败',
											content: res.msg
										});
									}
								});
						}
					}
				});
			},
			// 待支付订单支付
			orderpay() {
				let that = this
				if (this.openWay == 1) { //支付宝
					// #ifdef H5
					let data = {
						order_id: this.orderId,
						pay_type: 'alipay',
						method: 'wap',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(red => {
						if (red.code == 1) {
							const div = document.createElement('div')
							div.innerHTML = red.data //此处form就是后台返回接收到的数据
							document.body.appendChild(div)
							document.forms[0].submit()
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef APP
					let data = {
						order_id: this.orderId,
						pay_type: 'alipay',
						method: 'app',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(red => {
						if (red.code == 1) {
							this.setPayment('alipay', red.data);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
				} else if (this.openWay == 2) { //2微信
					// #ifdef MP-WEIXIN
					let data = {
						order_id: this.orderId,
						pay_type: 'wechat',
						method: 'miniapp',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(red => {
						if (red.code == 1) {
							uni.requestPayment({
								provider: 'wxpay',
								timeStamp: red.data.timeStamp,
								nonceStr: red.data.nonceStr,
								package: red.data.package,
								signType: red.data.signType,
								paySign: red.data.paySign,
								success: function(redd) {
									uni.showLoading({
										title: '支付成功'
									});
									uni.hideLoading();
									that.getDetail()
									setTimeout(function() {
										uni.navigateBack()
									}, 100)
								},
								fail: function(err) {
									uni.hideLoading();
									that.$queue.showToast(
										'支付失败');
								}
							});
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef H5
					let data = {
						pay_type: 'wechat',
						order_id: this.orderId,
						method: 'wap',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(red => {
						if (red.code == 1) {
							this.callPay(red);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef APP
					let data = {
						pay_type: 'alipay',
						order_id: this.orderId,
						method: 'app',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(red => {
						if (red.code == 1) {
							that.setPayment('wxpay', JSON.stringify(red.data));
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
				} else if (this.openWay == 3) { //3零钱
					let data = {
						pay_type: 'lingqian',
						order_id: this.orderId,
						method: 'lingqian',
					}
					this.$Request.postT('/api/index/user/user_go_pay', data).then(res => {
						if (res.code == 1) {
							uni.showToast({
								title: res.msg,
								icon: 'none',
								duration: 1200
							})
							uni.hideLoading();
							this.getDetail()
							setTimeout(function() {
								uni.navigateBack()
							}, 100)

						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}

			},
			// 加时支付
			pay() {
				let that = this
				if (!this.enddata || this.enddata == '') {
					uni.showToast({
						title: '请选择结束时间',
						icon: 'none'
					})
					return
				}
				if (this.openWay == 1) { //支付宝
					// #ifdef H5
					let data = {
						order_id: this.orderId,
						pay_type: 'alipay',
						method: 'wap',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(red => {
						if (red.code == 1) {
							const div = document.createElement('div')
							div.innerHTML = red.data //此处form就是后台返回接收到的数据
							document.body.appendChild(div)
							document.forms[0].submit()
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef APP
					let data = {
						order_id: this.orderId,
						pay_type: 'alipay',
						method: 'app',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(red => {
						if (red.code == 1) {
							this.setPayment('alipay', red.data);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
				} else if (this.openWay == 2) { //2微信
					// #ifdef MP-WEIXIN
					let data = {
						order_id: this.orderId,
						pay_type: 'wechat',
						method: 'miniapp',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(red => {
						if (red.code == 1) {
							uni.requestPayment({
								provider: 'wxpay',
								timeStamp: red.data.timeStamp,
								nonceStr: red.data.nonceStr,
								package: red.data.package,
								signType: red.data.signType,
								paySign: red.data.paySign,
								success: function(redd) {
									uni.showLoading({
										title: '支付成功'
									});
									uni.hideLoading();
									that.getDetail()
									setTimeout(function() {
										uni.navigateBack()
									}, 100)
								},
								fail: function(err) {
									uni.hideLoading();
									that.$queue.showToast(
										'支付失败');
								}
							});
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef H5
					let data = {
						pay_type: 'wechat',
						order_id: this.orderId,
						method: 'wap',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(red => {
						if (red.code == 1) {
							this.callPay(red);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
					// #ifdef APP
					let data = {
						pay_type: 'alipay',
						order_id: this.orderId,
						method: 'app',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(red => {
						if (red.code == 1) {
							that.setPayment('wxpay', JSON.stringify(red.data));
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					})
					// #endif
				} else if (this.openWay == 3) { //3零钱
					let data = {
						pay_type: 'lingqian',
						order_id: this.orderId,
						method: 'lingqian',
						end_time: this.enddata,
						amount: this.timePrice
					}
					this.$Request.postT('/api/index/order/user_extend_time', data).then(res => {
						if (res.code == 1) {
							uni.showToast({
								title: res.msg,
								icon: 'none',
								duration: 1200
							})
							uni.hideLoading();
							this.showHour = false
							this.getDetail()
							setTimeout(function() {
								uni.navigateBack()
							}, 100)

						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}

			},
			callPay: function(response) {
				if (typeof WeixinJSBridge === "undefined") {
					if (document.addEventListener) {
						document.addEventListener('WeixinJSBridgeReady', this.onBridgeReady(response), false);
					} else if (document.attachEvent) {
						document.attachEvent('WeixinJSBridgeReady', this.onBridgeReady(response));
						document.attachEvent('onWeixinJSBridgeReady', this.onBridgeReady(response));
					}
				} else {
					this.onBridgeReady(response);
				}
			},
			onBridgeReady: function(response) {
				let that = this;
				if (!response.package) {
					return;
				}
				WeixinJSBridge.invoke(
					'getBrandWCPayRequest', {
						"appId": response.appid, //公众号名称，由商户传入
						"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
						"nonceStr": response.noncestr, //随机串
						"package": response.package,
						"signType": response.signType, //微信签名方式：
						"paySign": response.sign //微信签名
					},
					function(res) {
						if (res.err_msg === "get_brand_wcpay_request:ok") {
							// 使用以上方式判断前端返回,微信团队郑重提示：
							//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
							uni.removeStorageSync('EditAddress')
							that.paySel = false;
							uni.showLoading({
								title: '支付成功'
							});
							uni.hideLoading();
							that.showHour = false
							that.getDetail()
							setTimeout(function() {
								uni.navigateBack()
							}, 100)
						} else {
							that.paySel = false;
							uni.hideLoading();
						}
						WeixinJSBridge.log(response.err_msg);
					}
				);
			},
			setPayment(name, order) {
				let that = this;
				uni.requestPayment({
					provider: name,
					orderInfo: order, //微信、支付宝订单数据
					success: function(res) {
						that.paySel = false;
						uni.removeStorageSync('EditAddress')
						uni.showLoading({
							title: '支付成功'
						});
						uni.hideLoading();
						that.getDetail()
						that.showHour = false
						setTimeout(function() {
							uni.navigateBack()
						}, 100)
					},
					fail: function(err) {
						that.paySel = false;
						uni.hideLoading();
						console.log(12)
					}
				});
			}
		}
	}
</script>

<style lang="less">
	page {
		background: #F5F5F5;
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin: 10rpx 30rpx;
	}


	.titl {
		font-size: 28rpx;
		font-family: PingFang SC;
		font-weight: 600;
	}

	.towbox {
		display: flex;
		align-items: center;

		image {
			width: 100rpx;
			height: 100rpx;
			border-radius: 8rpx;
		}

		.tit {
			width: 200rpx;
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			overflow: hidden;
		}

		.price {
			color: #333333;
			font-size: 24rpx;
			margin-top: 15rpx;
			font-weight: bold;

			text {
				font-size: 32rpx;
			}
		}
	}

	.line {
		width: 100%;
		background: #EEEEEE;
		height: 1rpx;
		margin: 30rpx 0;
	}

	.taber {
		background: #FFFFFF;
		padding: 20rpx 30rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 99;
		display: flex;
		align-items: center;
		justify-content: flex-end;

		.btn {
			width: 190rpx;
			height: 70rpx;
			border: 2rpx solid #01993A;
			border-radius: 40rpx;
			text-align: center;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #01993A;
			margin-right: 20rpx;
		}

		.btnn {
			width: 190rpx;
			height: 70rpx;
			background: #FDA310;
			border-radius: 40rpx;
			text-align: center;
			display: flex;
			align-items: center;
			justify-content: center;
			color: #FFFFFF;
		}
	}

	.btnn {
		width: 170rpx;
		height: 66rpx;
		line-height: 66rpx;
		background: linear-gradient(90deg, #452A16 0%, #71513C 100%);
		border-radius: 40rpx;
		text-align: center;
		display: flex;
		// align-items: center;
		justify-content: center;
		color: #FFFFFF;
	}
</style>