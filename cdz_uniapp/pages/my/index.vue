<template>
	<view class="">
		<!-- 2.0.19支持autoBack，默认为false -->
		<!-- #ifndef H5 -->
		<u-navbar title="我的" :leftIcon="leftIcon" :bgColor="bgColor" placeholder>
		</u-navbar>
		<!-- #endif -->

		<view class="bgColorTop">
			<view class=" userbox">
				<view class="u-m-r-10 " style="margin-left: 10upx;">
					<image :src="avatar" style="width: 100rpx;height: 100rpx;border-radius: 100rpx;"
						@click="goNav('/pages/my/userinfo')"></image>
				</view>
				<view class=" padding-left-sm " v-if="!isLogin">
					<view class="u-font-18  text-bold" @click="goNav('/pages/my/userinfo')">
						<view class="margin-right-sm">
							<view class=" ">{{userName}}</view>
							<view class=" text-sm" style="font-weight: 500;" v-if="invitationCode">
								ID:{{invitationCode}}</view>
						</view>
					</view>
				</view>
				<view v-else class="text-xl padding-left-sm text-bold" @click="goLogin">
					登录
				</view>
			</view>

			<view class="numbox">
				<view class="numIte" @click="goNav('/my/wallet/index')">
					<view class="numIte_num">{{money?money:0}}</view>
					<view class="numIte_tit">可用余额</view>
				</view>
				<view class="numIte" @click="goNav('/pages/coupon/myCompon')">
					<view class="numIte_num">{{couponCount?couponCount:0}}</view>
					<view class="numIte_tit">优惠券</view>
				</view>
				<view class="numIte" @click="goNav('/my/jifen/index')">
					<view class="numIte_num">{{userIntegral?userIntegral:0}}</view>
					<view class="numIte_tit">积分</view>
				</view>
			</view>


			<view class="vipbox" v-if="XCXIsSelect != '否'">
				<view class="">
					<view class="viptit">
						<image src="/static/images/my/vipicon.png" class="vipicon"></image>
					</view>
					<!-- <view class="tit">
						充电优惠8.5折
						<text></text>
						立减券×5
					</view> -->
				</view>
				<view v-if="!isVip" class="btn-bg" @click="goNav('/my/vip/index')">去开通
					<image src="/static/images/my/vipright.png"></image>
				</view>
				<view v-if="isVip" class="btn-bg" @click="goNav('/my/vip/index')">已开通
					<image src="/static/images/my/vipright.png"></image>
				</view>
			</view>

		</view>

		<view class="tanbox ">
			<view class="tanboxTit">充电订单</view>
			<view class="tanbox_Item">
				<view class=" tanbox_ItemInfo " @click="goNavOrder(1)">
					<image src="../../static/images/my/cdz.png" mode="widthFix"></image>
					<view>充电中</view>
				</view>
				<view class="tanbox_ItemInfo" @click="goNavOrder(2)">
					<image src="../../static/images/my/wzf.png" mode="widthFix"></image>
					<view>未支付</view>
				</view>
				<view class="tanbox_ItemInfo" @click="goNavOrder(3)">
					<image src="../../static/images/my/yjs.png" mode="widthFix"></image>
					<view>已结束</view>
				</view>
			</view>
		</view>


		<view class="boxtan ">

			<view class="image_tu" @click="goNav('/my/gird/index')" v-if="XCXIsSelect != '否'">
				<image src="../../static/images/my/cycdz.png" mode="aspectFill"></image>
				<view>常用电站</view>
			</view>
			<view v-if="isShop" class="image_tu" @click="goNav('/shop/index/index')">
				<image src="../../static/images/my/shopCenter.png" mode="aspectFill"></image>
				<view>商家中心</view>
			</view>

			<view class="image_tu" @click="goNav('/pages/my/invitationUser') " v-if="XCXIsSelect != '否'">
				<image src="../../static/images/my/yao.png"></image>
				<view>邀请好友</view>
			</view>
			<view class="image_tu" @click="goNav('/my/car/index')">
				<image src="../../static/images/my/car.png"></image>
				<view>我的车辆</view>
			</view>

			<view class="image_tu" @click="goChat()" v-if="XCXIsSelect != '否'">
				<image src="../../static/images/my/kf.png"></image>
				<view>联系客服</view>
			</view>
			<view class="image_tu" @click="goNav('/pages/feedback/index') ">
				<image src="../../static/images/my/ys.png"></image>
				<view>意见反馈</view>
			</view>
			<view class="image_tu" @click="goNav('/pages/feedbackIndex/feedbackIndex') ">
				<image src="../../static/images/my/xy.png"></image>
				<view>帮助中心</view>
			</view>
			<view class="image_tu" @click="goNav('/pages/member/about') ">
				<image src="../../static/images/my/about.png"></image>
				<view>关于我们</view>
			</view>

			<view class="image_tu" @click="goNav('/pages/my/set')">
				<image src="../../static/images/my/set.png"></image>
				<view>设置中心</view>
			</view>
		</view>

		<myTabbar :value="2" />
	</view>
</template>

<script>
	import myTabbar from '@/components/myTabbar.vue'
	export default {
		components: {
			myTabbar
		},
		data() {
			return {
				leftIcon: '',
				XCXIsSelect: '是',
				bgColor: 'linear-gradient(to right,#D9FEED ,#FBFDF0 60%)',
				isLogin: true,
				avatar: '',
				userName: '',
				invitationCode: '',
				isVip: false,
				couponCount: 0,
				money: 0,
				userIntegral: 0,
				isShop: false, //是否是商家
			}
		},
		onLoad() {

		},
		onShow() {
			let token = this.$queue.getData('token')
			if (token) {
				this.getShopCenter();
				this.isLogin = false
				this.getUserInfo();
				this.getDataInfo()
				this.getVip()
			} else {
				this.isLogin = true
				this.isVip = false
				this.avatar = '/static/logo.png'
				this.userName = '游客'
				this.invitationCode = ''
				this.couponCount = 0
				this.money = 0
				this.userIntegral = 0
				this.isShop = false
			}
		},
		methods: {
			getShopCenter() {
				if (uni.getStorageSync('token')) {
					let data = {
						page: 1,
						limit: 10,
						userId: uni.getStorageSync('userId')
					}
					this.$Request.getT('/app/tearoom/selectTearoomList', data).then(res => {
						if (res.code == 0) {
							if (res.data && res.data.list && res.data.list.length > 0) {
								this.isShop = true
							} else {
								this.isShop = false
							}
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					uni.navigateTo({
						url: '/pages/public/login'
					})
				}
			},
			// 获取是否vip
			getVip() {
				this.$Request.get("/app/UserVip/selectUserVip").then(res => {
					if (res.code == 0 && res.data) {
						this.isVip = res.data
					}
				});
			},
			goNav(url) {
				let token = uni.getStorageSync('token')
				if (token) {
					uni.navigateTo({
						url
					})
				} else {
					this.goLogin()
				}
			},
			goNavOrder() {
				let token = uni.getStorageSync('token')
				if (token) {
					uni.navigateTo({
						url: '/my/order/index'
					})
				} else {
					this.goLogin()
				}

			},
			goLogin() {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			},
			goChat() {
				let kefu = this.$queue.getData('SelKeFu'); // 用户端联系方式 1 手机号 2企业微信 
				let kefuPhone = this.$queue.getData('kefuPhone');
				let token = this.$queue.getData('token')
				if (token) {
					if (kefu == 1) {
						uni.makePhoneCall({
							phoneNumber: kefuPhone //仅为示例
						});
					} else if (kefu == 2) {
						// #ifdef MP-WEIXIN
						let that = this
						try {
							wx.openCustomerServiceChat({
								extInfo: {
									url: that.$queue.getData('SelKeFuLink')
								},
								corpId: that.$queue.getData('SelKeFuAppId'),
								success(res) {},
								fail(res) {
									console.error(res)
								}
							})
						} catch (error) {
							console.error("catchcatch" + error)
							uni.showToast({
								title: '请更新至微信最新版本'
							});
						}
						// #endif
						// #ifndef MP-WEIXIN
						let url = this.$queue.getData('SelKeFuLink');
						if (url.indexOf('/pages/') !== -1 || url.indexOf('/my/') !== -1) {
							uni.navigateTo({
								url
							});
						} else {
							//#ifndef H5
							uni.navigateTo({
								url: '/pages/index/webView?url=' + url
							});
							//#endif
							//#ifdef H5
							window.location.href = url;
							//#endif
						}
						// #endif
					} else if (kefu == 3) {
						uni.navigateTo({
							url: '/pages/my/customer'
						});
					}
				} else {
					uni.showModal({
						title: '提示',
						content: '您还未登录,请先登录',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/public/login'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			getUserInfo() {
				let userId = uni.getStorageSync('userId');
				if (!userId) {
					uni.navigateTo({
						url: '/pages/public/login'
					});
					return;
				}
				this.$Request.getT('/app/user/selectUserByUserId?userId=' + userId).then(res => {
					if (res.code == 0) {
						this.avatar = res.data.avatar ? res.data.avatar : '/static/logo.png';
						this.userName = res.data.userName ? res.data.userName : '游客';
						this.invitationCode = res.data.invitationCode
						this.$queue.setData('avatar', res.data.avatar);
					}
				});
			},
			getDataInfo() {
				let userId = uni.getStorageSync('userId');
				if (!userId) {
					uni.navigateTo({
						url: '/pages/public/login'
					});
					return;
				}
				this.$Request.getT('/app/userMoney/selectUserDetails').then(res => {
					if (res.code == 0) {
						this.couponCount = res.data.couponCount
						this.money = res.data.money
						this.userIntegral = res.data.userIntegral
					}
				});
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #F2F6FC;
	}

	.bgColorTop {
		width: 100%;
		background: #F2F6FC;
		position: relative;
		padding-top: 20rpx;
	}

	.bgColorTop::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to right, #D9FEED, #FBFDF0 60%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.bgColorTop::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #F2F6FC);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}

	.userbox {
		position: relative;
		z-index: 9;
		display: flex;
		flex-direction: row;
		align-items: center;
		padding: 30rpx;
	}




	.numbox {
		position: relative;
		z-index: 9;
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin: 0 30rpx 30rpx;

		.numIte {
			text-align: center;

			.numIte_num {
				font-family: DIN;
				font-weight: bold;
				font-size: 38rpx;
				color: #1E1E1E;
			}

			.numIte_tit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 26rpx;
				color: #333333;
			}
		}
	}

	.vipbox {
		position: relative;
		z-index: 9;
		height: 130rpx;
		padding: 0rpx 30rpx;
		// background: linear-gradient(90deg, #383733 0%, #191511 100%);
		background-image: url('https://myydj.mianyazhiyu.com/file/uploadPath/2025/09/15/7f1d20a265aa6e640229de988235ec69.png');
		background-size: 100% 100%;
		border-radius: 32rpx 32rpx 0rpx 0rpx;
		margin: 0 30rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.viptit {
			display: flex;
			align-items: center;

			.vipicon {
				width: 180rpx;
				height: 29rpx;
			}

			.viptitle {
				width: 172rpx;
				height: 28rpx;
			}
		}

		.tit {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 24rpx;
			color: #914016;
			margin-top: 8rpx;
			display: flex;
			align-items: center;

			text {
				display: inline-flex;
				width: 1rpx;
				height: 23rpx;
				background: #914016;
				margin: 0 10rpx;
			}
		}

		.btn-bg {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 24rpx;
			color: #914016;
			display: flex;
			align-items: center;

			image {
				width: 10rpx;
				height: 16rpx;
				margin-left: 10rpx;
			}
		}
	}


	.tanbox {
		background: #FFFFFF;
		padding: 30rpx;
		border-radius: 24rpx;
		margin: 30rpx;

		.tanboxTit {
			font-family: PingFang SC;
			font-weight: bold;
			font-size: 28rpx;
			color: #272727;
		}

		.tanbox_Item {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;

			.tanbox_ItemInfo {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #000000;
				text-align: center;

				image {
					width: 52rpx;
					height: 52rpx;
				}
			}
		}


	}




	.boxtan {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx;
		display: flex;
		flex-wrap: wrap;
		padding-top: 30rpx;

		.image_tu {
			width: 25%;
			text-align: center;
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #1A1A1A;
			padding-bottom: 30rpx;
			position: relative;

			image {
				width: 50rpx;
				height: 50rpx;
			}

			.image_tuIcon {
				position: absolute;
				top: 0;
				right: 0;
			}
		}
	}
</style>