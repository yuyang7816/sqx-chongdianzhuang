<template>
	<view class="">
		<view class="orderTop">
			<view class="orderTop_title" v-if="orderInfo.status==1">订单待支付</view>
			<view class="orderTop_title" v-if="orderInfo.status==3">订单使用中</view>
			<view class="orderTop_title" v-if="orderInfo.status==4">订单已完成</view>
			<view class="orderTop_title" v-if="orderInfo.status==5">订单已取消</view>
			<!-- <view class="orderTop_title" v-if="orderInfo.status==6">订单待评价</view> -->

			<view class="orderTop_tit" v-if="orderInfo.status==4">感谢您及时挪车的文明行为</view>
		</view>
		<view class="box">
			<view class="box_title" v-if="orderInfo.tearoom">{{orderInfo.tearoom.tearoomName}}</view>
			<view class="boxIte">
				<view>电费</view>
				<view>￥{{orderInfo.oldPayMoney}}</view>
			</view>
			<!-- <view class="boxIte">
				<view>服务费</view>
				<view>￥29</view>
			</view> -->
			<view class="boxIte" v-if="orderInfo.refundMoney">
				<view>退还金额</view>
				<view style="color: #FF3F3F;">-￥{{orderInfo.refundMoney}}</view>
			</view>
			<view class="boxItes">
				<view>实付总金额</view>
				<view style="color: #FF3F3F;">￥<text style="font-size:38rpx;">{{orderInfo.payMoney}}</text></view>
			</view>
		</view>

		<view class="box">
			<view class="box_title">订单信息</view>
			<view class="boxIte">
				<view>支付方式</view>
				<view v-if="orderInfo.payWay==1">微信支付</view>
				<view v-if="orderInfo.payWay==2">支付宝支付</view>
				<view v-if="orderInfo.payWay==3">钱包支付</view>
			</view>
			<view class="boxIte">
				<view>订单编号</view>
				<view>{{orderInfo.ordersNo}}
					<image @click="copyOrder(orderInfo.ordersNo)" src="../static/bianji.png"
						style="width:25rpx;height:25rpx;" mode="widthFix"></image>
				</view>
			</view>
			<view class="boxIte">
				<view>开始充电时间</view>
				<view>{{orderInfo.startTime}}</view>
			</view>
			<view class="boxIte" v-if="orderInfo.status==4">
				<view>结束充电时间</view>
				<view>{{orderInfo.endTime}}</view>
			</view>
			<view class="boxIte">
				<view>电桩编号</view>
				<view>{{orderInfo.goodsName}}</view>
			</view>
			<!-- <view class="boxIte">
				<view>充电电量</view>
				<view>45.516度</view>
			</view> -->
		</view>

		<view class="flex margin-lr justify-between">
			<view class="btn" @click="endOder()" v-if="orderInfo.status==3">结束订单</view>
			<view class="btn" @click="showPay = true" v-if="orderInfo.status==1">立即支付</view>
			<view class="btn" @click="cancelOrder()" v-if="orderInfo.status==1">取消订单</view>
		</view>


		<u-popup :show="showPay" :round="14" @close="showPay = false" closeable>
			<view class="paybox">
				<view class="payboxTit">支付方式</view>
				<view class="payboxItem " v-for="(item,index) in payType" :key='index'>
					<image :src="item.image" mode="aspectFill"></image>
					<view class="payboxItem_text">{{item.name}}
					</view>
					<radio-group name="openWay" style="margin-left: 20upx;" @tap='selectWay(item)'>
						<label class="tui-radio">
							<radio color="#5CC477" :checked="openWay === item.id ? true : false"
								style="transform:scale(0.8)" />
						</label>
					</radio-group>
				</view>

				<view class="btnpay" @click="pay()">立即支付</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import {
		showLoading
	} from '../../common/queue';

	export default {
		data() {
			return {
				ordersNo: '',
				orderInfo: {},
				price: 0,

				payType: [],
				openWay: 3,
				showPay: false
			}
		},
		onLoad(opion) {
			if (opion.ordersNo) {
				this.ordersNo = opion.ordersNo
				this.getDetail()
			}
			// #ifdef MP-WEIXIN
			this.payType = [{
					image: '/static/images/my/weixin.png',
					name: '微信支付',
					id: 1
				}, {
					image: '/static/images/my/lingqian.png',
					name: '钱包支付',
					id: 3
				}],
				this.openWay = 3
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				//公众号是否自动登录  108
				this.$Request.get('/app/common/type/108').then(res => {
					if (res.data && res.data.value && res.data.value == '是') {
						this.payType = [{
							image: '/static/images/my/weixin.png',
							name: '微信',
							id: 2
						}, {
							image: '/static/images/my/zhifubao.png',
							name: '支付宝',
							id: 1
						}, {
							image: '/static/images/my/lingqian.png',
							name: '钱包支付',
							id: 3
						}];
						this.openWay = 2;
					} else {
						this.payType = [{
							image: '/static/images/my/zhifubao.png',
							name: '支付宝',
							id: 1
						}, {
							image: '/static/images/my/lingqian.png',
							name: '钱包支付',
							id: 3
						}];
						this.openWay = 1;
					}
				})
			} else {
				this.payType = [{
					image: '/static/images/my/zhifubao.png',
					name: '支付宝',
					id: 1
				}, {
					image: '/static/images/my/lingqian.png',
					name: '钱包支付',
					id: 3
				}];
				this.openWay = 1;
			}
			// #endif

			// #ifdef APP
			this.payType = [{
				image: '/static/images/my/weixin.png',
				name: '微信支付',
				id: 1
			}, {
				image: '/static/images/my/zhifubao.png',
				name: '支付宝支付',
				id: 2
			}, {
				image: '/static/images/my/lingqian.png',
				name: '钱包支付',
				id: 3
			}]
			this.openWay = 3
			// #endif
		},
		onShow() {

		},
		methods: {
			copyOrder(order) {
				uni.setClipboardData({
					data: order,
					success: function() {
						that.$queue.showToast('复制成功')
					}
				})
			},
			getDetail() {
				this.$Request.get("/app/orders/selectOrdersDetails", {
					ordersNo: this.ordersNo
				}).then(res => {
					if (res.code == 0 && res.data) {
						this.orderInfo = res.data
						this.price = res.data.oldPayMoney - res.data.payMoney


					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			// 结束订单
			endOder() {
				uni.showLoading({
					title: '结束中...'
				})
				this.$Request.postT("/app/orders/closeOrders", {
					ordersId: this.orderInfo.ordersId
				}).then(res => {
					if (res.code == 0) {
						uni.hideLoading();
						uni.showToast({
							title: '订单已结束',
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			// 取消订单
			cancelOrder() {
				uni.showLoading({
					title: '取消中...'
				})
				this.$Request.postT("/app/orders/cancelOrders", {
					ordersNo: this.orderInfo.ordersNo
				}).then(res => {
					if (res.code == 0) {
						uni.hideLoading();
						uni.showToast({
							title: '订单已取消',
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			selectWay: function(item) {
				this.openWay = item.id;
			},
			pay() {
				uni.showLoading({
					title: '支付中...'
				})

				this.showPay = false
				if (this.openWay == 1) {
					// #ifdef MP-WEIXIN
					this.$Request.postT("/app/wxPay/payOrders", { //微信接口 1app2公众号 3小程序
						ordersNo: this.orderInfo.ordersNo,
						classify: 3
					}).then(red => {
						if (red.code == 0) {
							uni.requestPayment({
								provider: 'wxpay',
								timeStamp: red.data.timestamp,
								nonceStr: red.data.noncestr,
								package: red.data.package,
								signType: red.data.signType,
								paySign: red.data.sign,
								success: function(redd) {
									uni.showLoading({
										title: '支付成功'
									});
									this.getDetail()
								},
								fail: function(err) {
									uni.hideLoading();
									uni.showToast({
										title: '支付失败'
									})
								}
							});

						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					});
					// #endif
					// #ifdef APP
					this.$Request.postT("/app/wxPay/payOrders", { //微信接口 1app2公众号 3小程序
						ordersNo: this.orderInfo.ordersNo,
						classify: 1
					}).then(red => {
						if (red.code == 0) {
							this.setPayment('wxpay', JSON.stringify(red.data));
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					});
					// #endif

					// #ifdef H5
					this.$Request.postT("/app/wxPay/payOrders", { //微信接口 1app2公众号 3小程序
						ordersNo: this.orderInfo.ordersNo,
						classify: 2
					}).then(red => {
						if (red.code == 0) {
							this.callPay(red.data);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					});
					// #endif

				} else if (this.openWay == 2) {
					// #ifdef APP
					this.$Request.postT("/app/aliPay/payOrders", { //微信接口 1app2公众号 3小程序
						ordersNo: this.orderInfo.ordersNo,
						classify: 2
					}).then(red => {
						if (red.code == 0) {
							this.setPayment('alipay', red.data);
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
					});
					// #endif

					// #ifdef H5
					this.$Request.postT("/app/aliPay/payOrders", { //微信接口 1app2公众号 3小程序
						ordersNo: this.orderInfo.ordersNo,
						classify: 2
					}).then(red => {
						if (red.code == 0) {
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
					});
					// #endif

				} else if (this.openWay == 3) {
					this.$Request.postT("/app/orders/payOrders", {
						ordersNo: this.orderInfo.ordersNo,
					}).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title: '支付成功',
								icon: 'none'
							})
							this.getDetail()
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
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
							uni.showLoading({
								title: '支付成功'
							});
							uni.hideLoading();
							setTimeout(function() {
								uni.hideLoading();
								that.getDetail()
							}, 1000)
						} else {
							uni.hideLoading();
							setTimeout(function() {
								uni.hideLoading();
							}, 1000)
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
						uni.removeStorageSync('EditAddress')
						uni.showLoading({
							title: '支付成功'
						});
						uni.hideLoading();
						setTimeout(function() {
							uni.hideLoading();
							that.getDetail()
						}, 1000)
					},
					fail: function(err) {
						uni.hideLoading();
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.orderTop {

		background: #5CC477;
		padding: 0 40rpx 30rpx;

		.orderTop_title {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 44rpx;
			color: #FFFFFF;
			padding-top: 30rpx;
		}

		.orderTop_tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 32rpx;
			color: #FEFEFE;
			margin-top: 10rpx;
		}
	}

	.box {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.box_title {
			font-family: Microsoft YaHei;
			font-weight: bold;
			font-size: 32rpx;
			color: #232323;
		}

		.boxIte {
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 28rpx;
			color: #191919;
			margin-top: 30rpx;
		}

		.boxItes {
			display: flex;
			align-items: center;
			justify-content: space-between;
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 30rpx;
			color: #191919;
			margin-top: 30rpx;
		}
	}

	.btn {
		background: #5CC477;
		color: #FFFFFF;
		flex: 1;
		margin: 60rpx 15rpx;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 12rpx;
	}

	.paybox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;


		.payboxTit {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 30rpx;
			color: #333333;
		}

		.payboxItem {
			display: flex;
			align-items: center;
			justify-content: space-between;
			height: 90rpx;

			image {
				width: 58rpx;
				height: 58rpx;
			}

			.payboxItem_text {
				flex: 1;
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 30rpx;
				color: #333333;
				margin-left: 15rpx;
			}
		}

		.btnpay {
			background: #5CC477;
			color: #FFFFFF;
			margin: 40rpx 0;
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			border-radius: 12rpx;
		}
	}
</style>