<template>
	<view style="padding-bottom: 100px">
		<u-navbar :title="title" leftIconColor="#FFFFFF" :border-bottom="false" :autoBack="true"
			:bgColor="!fixeds?background:backgrounds" placeholder></u-navbar>
		<view class="bgvip">
			<image src="../static/Vipbg.png"></image>
		</view>
		<view class="text-white" style="position: relative;z-index: 9;">
			<view class="flex align-center padding-lr">
				<view class="">
					<u-avatar :src="avatar" size="100"></u-avatar>
				</view>
				<view class="margin-left-sm ">
					<view class="text-lg text-white text-bold">{{userName}}</view>
					<view class="text-sm  " style="color:#DBDBDB;" v-if="!isVip">您当前未开通VIP
					</view>
					<view class="text-sm  " style="color:#DBDBDB;" v-if="isVip&&vipEndTime">
						会员到期:{{vipEndTime}}</view>
				</view>
			</view>

			<view class="boxvip">
				<view class="text-center" style="color: #F6D7B7;">会员专属特权</view>
				<view class="flex  flex-wrap">
					<view v-for="(item,index) in MemberList" :key="index"
						style="width: 33%;text-align: center;margin-top: 34upx;">
						<image :src="item.memberImg" mode="" style="margin: 0 auto;height: 78rpx;width: 78rpx;"></image>
						<view class="margin-top-xs text-sm" style="color: #F6D7B7;">{{item.memberName}}</view>
						<view class="" style="color:#B1A598;font-size: 20rpx;">{{item.content}}</view>
					</view>
				</view>
			</view>
			<!-- <view class="juanbox">
				<view class="padding-lr flex align-center justify-between">
					<view class="title">超值礼包月月领</view>
				</view>
				<scroll-view class="scroll-view_H" scroll-x="true" @scroll="scroll" scroll-left="0">
					<view class="scroll-view-item_H" v-if="item" v-for="(item,index) in couponList" :key="index">
						<view class="juany"><text>￥</text>{{item.money}}</view>
						<view class="juanmj">满{{item.minMoney}}可用</view>
						<view class="juanline"></view>
						<view class="junti">{{item.couponName}}</view>
					</view>
				</scroll-view>
			</view> -->

			<view class="vipprice">
				<view v-for="(item,index) in vipList" :key='index' @click="select(item,index)" class="pricebox"
					:class="selNumIndex ==index?'active': ''">
					<view class="pricename">{{item.vipName}}</view>
					<view class="price">¥<text class="">{{item.money}}</text>
					</view>
					<view class="oldprice" :style="selNumIndex ==index?'color:#84512A':''" v-if="item.oldMoney">
						￥{{item.oldMoney}}</view>
					<!-- <view class="newprice" v-if="selNum == item.id">新人优惠价</view> -->
				</view>
				<view style="width: 219rpx;height: 0;"></view>
				<view style="width: 219rpx;height: 0;"></view>
			</view>

			<view class="paybox">
				<view class="" style="font-size: 30rpx;">支付方式</view>
				<view class="flex align-center justify-between" v-for="(item,index) in openLists" :key='index'>
					<view class="flex align-center flex-sub margin-tb-sm">
						<image :src="item.image" style="width: 50rpx;height:50rpx;border-radius: 50upx;"></image>
						<view class="margin-left" style="font-size: 30rpx;">{{item.text}}</view>
					</view>
					<view @tap='selectWay(item.id)'>
						<image src="../static/inall.png" style="width: 38rpx;height: 38rpx;" v-if="openWay != item.id">
						</image>
						<image src="../static/all.png" style="width: 38rpx;height: 38rpx;" v-else></image>
					</view>
				</view>
			</view>
		</view>

		<view class="btn" @tap="pay()">立即开通</view>
		<view class="flex align-center justify-center margin-lr margin-top">
			<view class="flex align-center text-sm text-white" @click='tapshowAgree'>
				<image src="../static/inall.png" style="width: 28rpx;height: 28rpx;" v-if="!showAgree">
				</image>
				<image src="../static/all.png" style="width: 28rpx;height: 28rpx;" v-else></image>
				<text class="margin-left-xs">我已阅读并同意</text>
				<!-- 协议地址 -->
				<navigator url="/my/vip/vipxieyi" open-type="navigate">《会员服务协议》</navigator>
			</view>

		</view>

	</view>
</template>

<script>
	export default {
		//判断粘性布局是否触顶
		onPageScroll(e) {
			console.log(e.scrollTop)
			if (Number(e.scrollTop) >= 30) {
				this.fixeds = true
			} else {
				this.fixeds = false
			}
		},
		data() {
			return {
				fixeds: false,
				showAgree: true,
				background: 'transparent',
				backgrounds: '#131217',
				
				gridData: [],
				avatar: '',
				userName: '匿名',
				vipList: [],
				couponList: [],
				selNumIndex: 0,
				selNum: 2,
				newPrice: 0,
				money: 0,
				price: 0,
				MemberList: [],
				isVip: false,
				vipEndTime: '',

				openWay: 2,
				openLists: [],
				title: ''

			}
		},
		onLoad() {
			this.avatar = uni.getStorageSync('avatar')
			this.userName = uni.getStorageSync('userName')

			this.getVipList()
			this.getMemberList()
			this.getMoney()
			this.getIsVip()
			this.getUserInfo()

			// #ifdef APP-PLUS
			this.openLists = [{
				image: '../../static/images/my/zhifubao.png',
				text: '支付宝',
				id: 1
			}, {
				image: '../../static/images/my/weixin.png',
				text: '微信',
				id: 2
			}];
			this.openWay = 2;
			// #endif

			// #ifdef MP-WEIXIN
			this.openLists = [{
				image: '../../static/images/my/weixin.png',
				text: '微信',
				id: 2
			}];
			this.openWay = 2;
			// #endif

			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				//公众号是否自动登录  108
				this.$Request.get('/app/common/type/108').then(res => {
					if (res.data && res.data.value && res.data.value == '是') {
						this.openLists = [ {
							image: '../../static/images/my/weixin.png',
							text: '微信',
							id: 2
						}];
						this.openWay = 2;
					} else {
						this.openLists = [{
							image: '../../static/images/my/zhifubao.png',
							text: '支付宝',
							id: 1
						}];
						this.openWay = 1;
					}
				})
			} else {
				this.openLists = [{
					image: '../../static/images/my/zhifubao.png',
					text: '支付宝',
					id: 1
				}];
				this.openWay = 1;
			}
			// #endif

		},
		methods: {
			tapshowAgree() {
				console.log('8888')
				this.showAgree = !this.showAgree
			},
			selectWay(id) {
				this.openWay = id
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png'
						this.vipEndTime = res.data.vipEndTime
						uni.setStorageSync('avatar', res.data.avatar)
					}
				});
			},
			//获取VIP列表
			getVipList() {
				this.$Request.get('/app/VipDetails/selectVipDetails').then(res => {
					if (res.code == 0) {
						this.vipList = res.data
						this.price = this.vipList[0].money
						this.selNum = this.vipList[0].id
						// this.couponList = this.vipList[0].couponList
					}
				})
			},
			// 获取特权列表
			getMemberList() {
				this.$Request.get('/app/member/selectMemberList').then(res => {
					if (res.code == 0) {
						this.MemberList = res.data
					}
				})
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						this.isVip = res.data
					}
				});
			},
			// 我的金元
			getMoney() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0 && res.data) {
						this.money = res.data.money
					}
				});
			},
			select(item, index) {
				console.log(item)
				this.selNumIndex = index
				// this.selNum = item.id
				// this.price = item.money
				console.log(this.vipList)
				// this.couponList = this.vipList[this.selNumIndex].couponList
			},
			pay() {
				if (!this.showAgree) {
					uni.showToast({
						title: '阅读并同意协议政策和服务协议',
						icon: 'none'
					})
					return
				}
				uni.showLoading({
					title: '支付中...'
				});
				let selNum = this.vipList[this.selNumIndex] ? this.vipList[this.selNumIndex].id : ''
				if (this.openWay == 2) { //微信

					let that = this
					// #ifdef MP-WEIXIN
					that.$Request.post("/app/wxPay/payMember", {
						vipDetailsId: selNum,
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
									that.isVip = true
									uni.showToast({
										title: '开通成功！'
									})
									setTimeout(function() {
										uni.navigateBack()
									}, 1000)
								},
								fail: function(err) {
									that.paySel = false;
									uni.hideLoading();
									that.$queue.showToast(
										'支付失败');
								}
							});
						} else {
							that.paySel = false;
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							uni.hideLoading();
						}, 2000);
					});
					// #endif

					// #ifdef H5
					that.$Request.post("/app/wxPay/payMember", {
						vipDetailsId: selNum,
						classify: 2
					}).then(red => {
						if (red.code == 0) {
							that.callPay(red.data);
						} else {
							that.paySel = false;
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							uni.hideLoading();
						}, 2000);
					});
					// #endif

					// #ifdef APP-PLUS
					that.$Request.post("/app/wxPay/payMember", {
						vipDetailsId: selNum,
						classify: 1
					}).then(red => {
						if (red.code == 0) {
							console.log(JSON.stringify(red))
							that.setPayment('wxpay', JSON.stringify(red
								.data));
						} else {
							that.paySel = false;
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							uni.hideLoading();
						}, 2000);
					});
					// #endif
				} else if (this.openWay == 1) { //支付宝
					let that = this
					// #ifdef H5
					that.$Request.post("/app/aliPay/payMember", {
						vipDetailsId: selNum,
						classify: 5
					}).then(red => {
						if (red.code == 0) {
							const div = document.createElement('div')
							div.innerHTML = red.data //此处form就是后台返回接收到的数据
							document.body.appendChild(div)
							document.forms[0].submit()
						} else {
							that.paySel = false;
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							uni.hideLoading();
						}, 2000);
					});
					// #endif
					// #ifdef APP-PLUS
					that.$Request.post("/app/aliPay/payMember", {
						vipDetailsId: selNum,
						classify: 4
					}).then(red => {
						if (red.code == 0) {
							that.setPayment('alipay', red.data);
						} else {
							that.paySel = false;
							uni.showToast({
								title: red.msg,
								icon: 'none'
							})
						}
						setTimeout(function() {
							uni.hideLoading();
						}, 2000);
					});
					// #endif
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
							that.isVip = true
							uni.showToast({
								title: '开通成功！'
							})
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
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
						that.isVip = true
						uni.showToast({
							title: '开通成功！'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					},
					fail: function(err) {
						that.paySel = false;
						uni.hideLoading();
						console.log(12)
					}
				});
			}
		},
		computed: {

		}
	}
</script>

<style lang="less">
	page {
		background-color: #131217;
	}

	.text-white {
		color: #FFFFFF;
	}

	.bgvip {
		width: 100%;
		height: 295rpx;
		position: fixed;
		top: 0;

		image {
			width: 100%;
			height: 100%;
		}
	}

	.boxvip {
		width: calc(100% - 60rpx);
		background: linear-gradient(90deg, #272833 0%, #25262C 100%);
		border-radius: 32rpx;
		margin: 20rpx auto;
		padding: 30rpx 0;
	}

	.juanbox {
		width: calc(100% - 60rpx);
		margin: 20rpx auto;
		background: #25272D;
		border-radius: 24rpx;
		padding: 30rpx 0;

		.title {
			font-style: italic;
			font-family: REEJI-PinboGB-Flash;
			font-weight: 800;
			font-size: 30rpx;
			color: #F6D7B7;

		}

		.titl {
			font-family: PingFang SC;
			// font-weight: bold;
			font-size: 26rpx;
			color: #F6D7B7;
		}

		.scroll-view_H {
			white-space: nowrap;
			width: 100%;
			padding-left: 30rpx;
			margin-top: 30rpx;

			.scroll-view-item_H {
				display: inline-block;
				width: 166rpx;
				height: 208rpx;
				background: url('../static/vipjuan.png');
				background-size: 100% 100%;
				background-repeat: no-repeat;
				text-align: center;
				margin-right: 20rpx;

				.juany {
					font-family: Source Han Sans CN;
					font-weight: 800;
					font-size: 36rpx;
					color: #84512A;
					margin-top: 30rpx;

					text {
						font-size: 26rpx;
						font-weight: 500;
					}
				}

				.juanmj {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 24rpx;
					color: #84512A;
				}

				.juanline {
					width: 130rpx;
					height: 1rpx;
					background: #DCB57C;
					margin: 30rpx auto 15rpx;
				}

				.junti {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 26rpx;
					color: #84512A;
				}
			}
		}

	}

	.vipprice {
		width: calc(100% - 60rpx);
		margin: 30rpx auto 0;
		display: flex;
		align-items: center;
		flex-wrap: wrap;
		justify-content: space-between;

		.pricebox {
			width: 219rpx;
			height: 260rpx;
			background: linear-gradient(90deg, #272833 0%, #25262C 100%);
			border-radius: 24rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			color: #FFFFFF;
			position: relative;

			.newprice {
				background: linear-gradient(to right, #E6BD78, #ECD1A0, );
				padding: 4rpx 20rpx;
				border-radius: 35rpx 0 35rpx 0;
				font-size: 26rpx;
				color: #755426;
				position: absolute;
				top: -10rpx;
				left: 0;
			}

			.pricename {
				font-family: PingFang SC;
				font-weight: bold;
				font-size: 32rpx;

			}

			.price {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 30rpx;

				text {
					font-size: 60rpx;
					font-weight: bold;
					margin-left: 8rpx;
				}
			}

			.oldprice {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 26rpx;
				color: #B3B3B3;
				text-decoration-line: line-through;
			}
		}

		.active {
			color: #84512A;
			background: linear-gradient(-32deg, #EFC69A 0%, #FCDEAC 53%, #FFE8C9 100%);

		}
	}

	.paybox {
		background: linear-gradient(90deg, #272833 0%, #25262C 100%);
		width: calc(100% - 60rpx);
		margin: 20rpx auto 0;
		border-radius: 24rpx;
		padding: 30rpx 30rpx 20rpx;
	}

	.btn {
		width: calc(100% - 60rpx);
		margin: 60rpx auto 0;
		height: 100rpx;
		background: linear-gradient(90deg, #F2E0C4 0%, #EABE7B 100%);
		border-radius: 50rpx;
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 32rpx;
		color: #56260A;
		display: flex;
		align-items: center;
		justify-content: center;
	}
</style>