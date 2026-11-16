<template>
	<view class="">
		<view class="yfbox">
			<view class="yfboxTitle">选择预付金额</view>
			<view class="flex flex-wrap justify-between margin-top-sm">
				<view class="yfboxItem" v-for="(item,index) in yflist" :key="index" @click="changeyf(item,index)"
					:class="listIndex==index?'yfboxItemAct':''">
					<view class="yfboxItem_num"><text>{{item.price}}</text>{{item.unit}}</view>
					<view class="yfboxItem_tit">{{item.title}}</view>
				</view>
			</view>
			<view class="yfboxTit">
				<view>电费单价:{{price}}/时</view>
				<!-- <view>充电价格:{{totalPrice}}/度</view> -->
			</view>
			<view class="yfboxTits " >
				
				<view>支付金额:<text style="font-size: 24rpx;">￥</text><text>{{totalPrice}}</text></view>
			</view>

			<view class="yfboxyhq">
				<view class="yfboxyhq_title">优惠券</view>
				<view class="yfboxyhq_pla" v-if="couponName" @click="openCoupon()">
					<view class="yfboxyhq_pla_title">{{couponName}}</view>
					<view class="margin-left-xs" @click.stop="closeCoupon()">
						<u-icon name="close-circle-fill" color="#999999" size="12"></u-icon>
					</view>
				</view>
				<view class="yfboxyhq_pla" v-else @click="openCoupon()">
					<view class="yfboxyhq_pla_tit">{{list.length==0?'暂无可使用优惠券':'选择优惠券'}}</view>
					<u-icon name="arrow-right" color="#999999" size="12"></u-icon>
				</view>
			</view>
		</view>

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
		</view>

		<view class="Tipbox">
			<view class="TipboxTit">提示: </view>
			{{content}}
			<!-- <text v-html="content"></text> -->
		</view>
		<view class="btn" @click="pay">
			立即充电
		</view>

		<u-popup :show="couponShow" @close="couponShow = false" :round="16">
			<view class="couponbox">
				<view class="couponbox_title">优惠券</view>
				<scroll-view scroll-y="true" class="scroll-Y">

					<view class="list flex align-center justify-center" v-for="(item,index) in list">
						<view class="list-box">
							<image src="/static/compon/yhqBgimg.png"></image>
							<view class="list-box-info flex align-center justify-between">
								<view class="list-box-info-l">
									<view class="list-box-info-l-title">
										{{item.couponName}}
									</view>
									<view class="list-box-info-l-time">
										有效期至{{item.failureTime}}
									</view>
									<view class="list-box-info-l-line"></view>
									<view class="list-box-info-l-shop flex align-center">
										<block>
											适用门店:通用
										</block>
										<view v-if="item.teamRule" @click="showGZ(item)" class="flex align-center"
											style="margin-left: 20rpx;color: #58B530;">
											使用规则
											<view class="" style="margin-left: 10rpx;">
												<u-icon name="arrow-down" color="#58B530" size="16"></u-icon>
											</view>
										</view>
									</view>
								</view>
								<block>
									<block>
										<view class="" @tap="userCoupon(item)" v-if="item.status == 1">
											<view class="list-box-info-r flex align-center justify-center">
												使用
											</view>
										</view>
									</block>
								</block>
							</view>
						</view>
					</view>

				</scroll-view>
			</view>
		</u-popup>
		<u-popup :round="20" :safeAreaInsetBottom="false" closeable :show="showXz" @close="closeXz" mode="center">
			<view class="showXz_view">
				<view class="showXz-title">
					使用规则
				</view>
				<view v-if="teamRule">
					<view class="showXz-contont">
						{{teamRule}}
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				payType: [],
				openWay: 3,

				yflist: [],
				listIndex: 0,
				tearoomId: '', //房间id
				goodsId: '', //插件id
				current: -1,
				price: 0,
				isVip: false,
				totalPrice: 0,
				content: '',

				list: [],
				couponShow: false,
				couponName: '',
				couponId: '',

				teamRule: '',
				showXz: false,

			}
		},
		onLoad(option) {
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId
			}
			if (option.goodsId) {
				this.goodsId = option.goodsId

			}
			if (option.current) {
				this.current = option.current
			}
			// #ifdef MP-WEIXIN
			console.log(option)
			// const scene = decodeURIComponent(option.scene);
			let scene = option.scene
			this.tearoomId = scene.split(',')[0]
			this.goodsId = scene.split(',')[1]
			let tearoomType = scene.split(',')[2] //1 电瓶车 2汽车桩
			this.current = tearoomType == 1 ? 0 : 1

			// #endif
			// 电动车充电配置	1163
			this.$Request.getT('/app/common/type/1163').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						let arr = []
						res.data.value.split(',').forEach(d => {
							let data = {}
							data.price = d
							data.title = '按小时计费'
							data.unit = '时'
							arr.push(data)
						})
						this.yflist = arr
						if (this.yflist.length != 0) {
							this.getAllprice(this.yflist[0].price)
						}
					}
				}
			});
			// 充电协议	1164
			this.$Request.getT('/app/common/type/1164').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.content = res.data.value
					}
				}
			});

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
			this.getVip()
			if (this.tearoomId) {


			}
		},
		methods: {
			// 清空优惠券
			closeCoupon() {
				this.couponName = ''
				this.couponId = ''
			},
			// 点击使用优惠券
			userCoupon(item) {
				this.couponName = item.couponName
				this.couponId = item.couponUserId
				this.couponShow = false
			},
			// 打开选择优惠券弹框
			openCoupon() {
				if (this.list.length != 0) {
					this.couponShow = true
				} else {
					uni.showToast({
						title: '暂无可使用优惠券',
						icon: 'none'
					})
				}
			},
			// 优惠券使用规则
			showGZ(item) {
				this.teamRule = item.teamRule;
				console.log(this.teamRule)
				this.showXz = true;
			},
			closeXz() {
				this.showXz = !this.showXz
			},
			//获取优惠券列表
			getUseList() {
				let data = {
					status: 1,
					page: 0,
					size: 50,
					money: this.totalPrice,
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.status == 0) {
						this.list = res.data.content
					}
				})
			},
			changeyf(item, index) {
				this.listIndex = index
				this.getAllprice(item.price)
			},
			getAllprice(hour) {
				let data = {
					goodsId: this.goodsId,
					hour: hour
				}
				this.$Request.get("/app/orders/selectOrdersMoney", data).then(res => {
					if (res.code == 0) {
						this.totalPrice = res.payMoney
						this.getPrice()
					}
				});
			},
			// 获取是否vip
			getVip() {
				this.$Request.get("/app/UserVip/selectUserVip").then(res => {
					if (res.code == 0 && res.data) {
						this.isVip = res.data
					}
				});
			},
			// 获取当前价格
			getPrice() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.get("/app/goodsTime/selectGoodsTimeByTime", data).then(res => {
					if (res.code == 0) {
						if (this.isVip) {
							this.price = res.data.memberHourPrice
						} else {
							this.price = res.data.hourPrice
						}
						this.getUseList()
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
				let hour = this.yflist[this.listIndex].price

				let data = {
					tearoomId: this.tearoomId,
					goodsId: this.goodsId,
					hour: hour,
					ordersType: this.current == 0 ? 1 : 2, //1 电动车  2 汽车
					couponId: this.couponId
				}
				this.$Request.postJson("/app/orders/insertOrders", data).then(res => {
					if (res.code == 0) {
						let ordersId = res.data.ordersNo
						// this.payOrder(ordersId)
						if (this.openWay == 2) {

							// #ifdef MP-WEIXIN
							this.$Request.postT("/app/wxPay/payOrders", { //微信接口 1app2公众号 3小程序
								ordersNo: ordersId,
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
											setTimeout(function() {
												uni.hideLoading();
												uni.redirectTo({
													url: '/my/order/index'
												})
											}, 1000)
										},
										fail: function(err) {
											uni.hideLoading();
											uni.showToast({
												title: '支付失败'
											})
											setTimeout(function() {
												uni.hideLoading();
												uni.redirectTo({
													url: '/my/order/index'
												})
											}, 1000)
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
								ordersNo: ordersId,
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
								ordersNo: ordersId,
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

						} else if (this.openWay == 1) {
							// #ifdef APP
							this.$Request.postT("/app/aliPay/payOrders", { //微信接口 1app2公众号 3小程序
								ordersNo: ordersId,
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
								ordersNo: ordersId,
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
								ordersNo: ordersId
							}).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: '支付成功',
										icon: 'none'
									})
									let that = this
									setTimeout(function() {
										uni.hideLoading();
										uni.redirectTo({
											url: '/my/order/index?current=' + that.current
										})
									}, 1000)

								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							});
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						let that = this
						setTimeout(function() {
							uni.hideLoading();
							uni.redirectTo({
								url: '/my/order/index?current=' + that.current
							})
						}, 1000)

					}
				});
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
							uni.showLoading({
								title: '支付成功'
							});
							uni.hideLoading();
							setTimeout(function() {
								uni.hideLoading();
								uni.redirectTo({
									url: '/my/order/index?current=' + that.current
								})
							}, 1000)
						} else {
							uni.hideLoading();
							setTimeout(function() {
								uni.hideLoading();
								uni.redirectTo({
									url: '/my/order/pay?id=' + that.ordersId
								})
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
							uni.redirectTo({
								url: '/my/order/index?current=' + that.current
							})
						}, 1000)
					},
					fail: function(err) {
						uni.hideLoading();
						setTimeout(function() {
							uni.hideLoading();
							uni.redirectTo({
								url: '/my/order/pay?id=' + that.ordersId
							})
						}, 1000)
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.yfbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.yfboxTitle {
			font-weight: 500;
			font-size: 30rpx;
			color: #232323;
		}

		.yfboxItem {
			width: 202rpx;
			height: 130rpx;
			background: #F2F2F2;
			border-radius: 16rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			margin-bottom: 20rpx;

			.yfboxItem_num {
				font-size: 24rpx;
				color: #191919;

				text {
					font-weight: bold;
					font-size: 38rpx;
				}
			}

			.yfboxItem_tit {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.yfboxItemAct {
			background: #5CC477;
			color: #FFFFFF;

			.yfboxItem_num {
				color: #FFFFFF;
			}

			.yfboxItem_tit {
				color: #FFFFFF;
			}
		}

		.yfboxTit {
			font-weight: 500;
			font-size: 26rpx;
			color: #32BE62;
			display: flex;
			align-items: center;
			justify-content: space-between;
		}
		.yfboxTits{
			font-weight: 500;
			font-size: 26rpx;
			color: #333333;
			display: flex;
			align-items: center;
			justify-content: end;
			text{
				font-size: 32rpx;
				color: #ff0000;
			}
		}
	}

	.yfboxyhq {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 20rpx;

		.yfboxyhq_title {
			font-weight: 500;
			font-size: 28rpx;
			color: #232323;
		}

		.yfboxyhq_pla {
			display: flex;
			align-items: center;

			.yfboxyhq_pla_title {
				font-weight: 400;
				font-size: 28rpx;
				color: #232323;
			}

			.yfboxyhq_pla_tit {
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;
			}
		}
	}


	.paybox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

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
	}

	.Tipbox {
		margin: 20rpx 30rpx;

		.TipboxTit {
			font-weight: 500;
			font-size: 24rpx;
			color: #FF130A;
		}

		text {
			font-weight: 500;
			font-size: 24rpx;
			color: #999999;
		}
	}

	.btn {
		height: 100rpx;
		background: #32BE62;
		border-radius: 50rpx;
		font-weight: 500;
		font-size: 30rpx;
		color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		letter-spacing: 4rpx;
	}

	.couponbox {
		border-radius: 24rpx;
		padding: 30rpx;
		background: #f8f8f8;

		.couponbox_title {
			font-size: 30rpx;
			text-align: center;
			font-weight: bold;
		}
	}

	.scroll-Y {
		height: 590rpx;
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.list-box {
			width: 686rpx;
			height: 228rpx;
			position: relative;

			image {
				width: 100%;
				height: 100%;
			}
		}

		.list-box-info {
			position: absolute;
			width: 100%;
			height: 228rpx;
			top: 0;
			left: 0;
			padding: 0 30rpx;

			.list-box-info-l {
				width: 428rpx;

				.list-box-info-l-title {
					color: #000000;
					font-size: 30rpx;
					font-weight: bold;
				}

				.list-box-info-l-time {
					color: #666666;
					font-size: 26rpx;
					margin-top: 10rpx;
				}

				.list-box-info-l-line {
					width: 100%;
					border-bottom: 1px dashed #D7D0C8;
					margin-top: 20rpx;
				}

				.list-box-info-l-shop {
					color: #666666;
					font-size: 24rpx;
					margin-top: 20rpx;
				}
			}

			.list-box-info-r-money-t {
				color: #FF6B00;
				font-size: 54rpx;

				text {
					font-size: 34rpx;
				}
			}

			.list-box-info-r-money-b {
				color: #666666;
				font-size: 24rpx;
				text-decoration-line: line-through;
			}

			.list-box-info-r {
				background-color: #58B530;
				color: #ffffff;
				font-size: 24rpx;
				width: 128rpx;
				height: 54rpx;
				border-radius: 27rpx;
			}
		}
	}

	.showXz_view {
		width: 600rpx;
		height: auto;
		padding: 30rpx 50rpx;
		color: #000000;

		.showXz-title {
			text-align: center;
			font-size: 32rpx;
			font-weight: bold;
			margin-bottom: 40rpx;
		}

		.showXz-titles {
			font-size: 30rpx;
			font-weight: bold;
		}

		.showXz-contont {
			padding: 20rpx 0;
		}
	}
</style>