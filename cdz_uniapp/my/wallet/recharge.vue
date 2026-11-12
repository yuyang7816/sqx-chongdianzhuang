<template>
	<view style="padding-bottom: 40rpx;">
		<view class="topMoney flex align-center justify-between">
			<view class="topMoney-l">
				<view class="topMoney-l-title flex align-center">
					当前余额
				</view>
				<view class="topMoney-l-money">
					{{money}}
				</view>
				<view @click="goNav('/my/wallet/mymoneydetail')" class="topMoney-l-tips flex align-center">
					明细记录<u-icon name="arrow-right" color="#FFFFFF" size="12"></u-icon>
				</view>
			</view>

		</view>
		<view class="payClassList flex justify-between flex-wrap">
			<view @click="selectPayCurr(index)" :class="current == index?'payClassListActive':''"
				class="payClassList-item flex align-center justify-center" v-for="(item,index) in payList" :key="index">
				<view class="payClassList-item-num flex align-center justify-center">
					<text>¥</text>
					<text>{{item.price}}</text>
				</view>
				<view v-if="item.giveMoney && item.giveMoney>0" class="payClassList-item-song">
					赠送{{item.giveMoney}}元
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

		<view class="xieyi flex align-center">
			<u-checkbox-group v-model="checkboxValue" @change="change">
				<u-checkbox :name="checked" shape="circle" activeColor="#5CC477"></u-checkbox>
			</u-checkbox-group>
			<view class="flex align-center">
				我已阅读并同意<text style="color: #5CC477;" @click="goPayMoneyXy()">《充值协议》</text>
			</view>
		</view>
		<view class="recharge_btn flex alic jc f32 cfff" @click="payMoney()">立即充值</view>



	</view>
</template>

<script>
	export default {

		data() {
			return {
				checkboxValue: [true],
				type: 1, //会员卡类型 1兑换 2转赠

				checked: true, //充值协议
				payList: [], //充值列表
				money: 0, //余额
				avatar: '',
				current: 0,


				vipIndex: 1,
				isVip: false, //是否是会员

				payType: [],
				openWay: 1,

			}
		},
		onLoad() {
			// #ifdef APP-PLUS
			this.payType = [{
				image: '../../static/images/my/zhifubao.png',
				name: '支付宝',
				id: 1
			}, {
				image: '../../static/images/my/weixin.png',
				name: '微信',
				id: 2
			}];
			this.openWay = 1;
			// #endif

			// #ifdef MP-WEIXIN
			this.payType = [{
				image: '../../static/images/my/weixin.png',
				name: '微信',
				id: 1
			}];
			this.openWay = 1;
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				//公众号是否自动登录  108
				this.$Request.get('/app/common/type/108').then(res => {
					if (res.data && res.data.value && res.data.value == '是') {
						this.payType = [{
							image: '../../static/images/my/weixin.png',
							name: '微信',
							id: 1
						}];
						this.openWay = 1;
					} else {
						this.payType = [{
							image: '../../static/images/my/zhifubao.png',
							name: '支付宝',
							id: 2
						}];
						this.openWay = 2;
					}
				})
			} else {
				this.payType = [{
					image: '../../static/images/my/zhifubao.png',
					name: '支付宝',
					id: 2
				}];
				this.openWay = 2
			}
			// #endif
		},
		onShow() {


			if (uni.getStorageSync('token')) {

				this.getMyMoney()
				this.getPaySelectList()
				this.getUserInfo()
			} else {
				this.vipIndex = 1
				this.isVip = false

			}
		},
		methods: {


			//获取我下单的折扣
			getVipPrivilegeDetails() {
				let data = {
					privilegeClassify: 2
				}
				this.$Request.getT('/app/vipPrivilege/selectVipPrivilegeDetails', data).then(res => {
					if (res.code == 0) {
						if (res.data && res.data.parent) {
							uni.setStorageSync('vipParent', res.data.parent)
						} else {
							uni.setStorageSync('vipParent', 1)
						}
					}
				})
			},

			//获取个人信息
			getUserInfo() {
				this.$Request.getT("/app/user/selectUserById").then(res => {
					if (res.code == 0) {

						this.avatar = res.data.avatar
						if (res.data.vipIndex) {
							this.vipIndex = res.data.vipIndex
						} else {
							this.vipIndex = 1
						}
						if (this.vipIndex > 1) {
							this.isVip = true
						} else {
							this.isVip = false
						}

					}
				});
			},
			goNav(url) {
				uni.navigateTo({
					url: url
				})
			},
			change(detail) {
				this.checkboxValue = detail
			},
			selectWay: function(item) {
				this.openWay = item.id;
			},
			//充值
			payMoney() {
				if (this.checkboxValue.length == 0) {
					uni.showToast({
						title: '请先阅读并同意《充值协议》',
						icon: 'none'
					})
					return
				}

				if (this.openWay == 1) {
					uni.showLoading({
						title: '支付中...'
					})
					// #ifdef MP-WEIXIN
					let data = {
						payClassifyId: this.payList[this.current].payClassifyId,
						classify: 3
					}
					this.$Request.post('/app/wxPay/payMoney', data).then(ret => {
						uni.hideLoading()
						let that = this
						uni.requestPayment({
							provider: 'wxpay',
							timeStamp: ret.data.timestamp,
							nonceStr: ret.data.noncestr,
							package: ret.data.package,
							signType: ret.data.signType,
							paySign: ret.data.sign,
							success: function(suc) {
								that.getMyMoney()
								uni.showToast({
									title: '支付成功',
									icon: 'success'
								})
							},
							fail: function(err) {
								uni.showToast({
									title: '支付失败',
									icon: 'none'
								})
							}
						});
					});
					// #endif
					// #ifdef APP
					let data = {
						payClassifyId: this.payList[this.current].payClassifyId,
						classify: 1
					}
					this.$Request.post('/app/wxPay/payMoney', data).then(red => {
						if (red.code == 0) {
							this.setPayment('wxpay', JSON.stringify(red.data));
						} else {
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						uni.hideLoading()
					});
					// #endif

					// #ifdef H5
					let data = {
						payClassifyId: this.payList[this.current].payClassifyId,
						classify: 2
					}
					this.$Request.post('/app/wxPay/payMoney', data).then(ret => {
						if (ret.code == 0) {
							this.callPay(ret.data);
						} else {
							uni.showToast({
								title: ret.msg,
								icon: 'none'
							})
						}
						uni.hideLoading()
					});
					// #endif

				} else if (this.openWay == 2) {
					// #ifdef APP
					this.$Request.postT("/app/aliPay/payMoney", { //微信接口 1app2公众号 3小程序
						payClassifyId: this.payList[this.current].payClassifyId,
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
					this.$Request.postT("/app/aliPay/payMoney", { //微信接口 1app2公众号 3小程序
						payClassifyId: this.payList[this.current].payClassifyId,
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
				}

			},
			//充值协议
			goPayMoneyXy() {
				uni.navigateTo({
					url: '/my/setting/payMoney'
				})
			},
			//选择充值
			selectPayCurr(index) {
				this.current = index
			},
			//获取余额
			getMyMoney() {
				this.getVipPrivilegeDetails()
				this.$Request.getT('/app/userMoney/selectMyMoney').then(res => {
					if (res.code == 0) {
						if (res.data.money > 0) {
							this.money = this.$queue.countMoney(res.data.money)
						}
					} else {
						this.money = 0
					}
				})
			},
			//获取充值列表
			getPaySelectList() {
				this.$Request.get('/app/payClassify/selectPayClassifyList').then(res => {
					if (res.code == 0) {
						this.payList = res.data
					}
				})
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
								that.getMyMoney();
							}, 1000)
						} else {
							that.paySel = false;
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
							that.getMyMoney();
						}, 1000)
					},
					fail: function(err) {
						uni.hideLoading();
						setTimeout(function() {
							uni.hideLoading();
						}, 1000)
					}
				});
			}
		}
	}
</script>

<style>
	page {
		background-color: #FFFFFF;
	}
</style>
<style lang="scss" scoped>
	.topMoney {
		width: 100%;
		padding: 40rpx 56rpx;
		background-color: #5CC477;
		position: relative;
		padding-bottom: 100rpx;

		.topMoney-l {
			color: #FFFFFF;

			.topMoney-l-title {
				font-size: 30rpx;
				font-weight: bold;
			}

			.topMoney-l-money {
				font-size: 68rpx;
				font-weight: 500;
				margin-top: 10rpx;
			}

			.topMoney-l-tips {
				font-size: 24rpx;
			}
		}

		.topMoney-r {
			width: 248rpx;
			height: 248rpx;
		}

		.topMoney-xf {
			position: absolute;
			right: 0;
			top: 10rpx;

			.topMoney-xf-item {
				background-color: rgba(0, 0, 0, 0.3);
				border-radius: 30rpx 0rpx 0rpx 30rpx;
				color: #FFFFFF;
				font-size: 24rpx;
				margin-top: 20rpx;
				padding: 14rpx 20rpx;
			}
		}
	}

	.payClassListActive {
		background-color: #dcffe3 !important;
		border: 1px solid #5CC477 !important;

		.payClassList-item-num {
			color: #5CC477 !important;
		}
	}

	.payClassList {
		padding: 60rpx 32rpx 0;
		background-color: #FFFFFF;
		border-radius: 32rpx 32rpx 0 0;
		position: relative;
		margin-top: -30rpx;

		.payClassList-item {
			width: 333rpx;
			height: 190rpx;
			background-color: #F4F4F4;
			border-radius: 16rpx;
			flex-direction: column;
			margin-top: 20rpx;
			position: relative;
		}

		.payClassList-item-tips {
			position: absolute;
			left: 0;
			top: 0;
			color: #8f6f08;
			background: #F9E5A4;
			padding: 4rpx 20rpx;
			font-size: 24rpx;
			border-radius: 16rpx 0 16rpx 0;
		}

		.payClassList-item-num {
			color: #494A46;

			text:nth-of-type(1) {
				font-size: 45rpx;
			}

			text:nth-of-type(2) {
				font-size: 58rpx;
				margin-left: 10rpx;
			}
		}

		.payClassList-item-song {
			color: #333333;
			font-size: 26rpx;
			margin-top: 10rpx;
		}
	}

	.xieyi {
		width: 686rpx;
		height: auto;
		margin: 10rpx auto;
		color: #333333;
		// margin-top: 40rpx;
	}

	.recharge_btn {
		width: 686rpx;
		height: 100rpx;
		margin: 0 auto;
		background: #5CC477;
		border-radius: 50rpx;
		margin-top: 40rpx;
	}


	.paybox {
		background: #FFFFFF;
		border-radius: 24rpx;
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
</style>