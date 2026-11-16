<template>
	<view>
		<view class="bg_view">
			<view class="topInfo flex align-center justify-center">
				<view class="topInfo-box flex align-center" @tap="goNav('/pages/my/userinfo')">
					<image :src="avatar ? avatar : '/static/logo.png'" mode="aspectFill"></image>
					<view class="topInfo-box-r flex align-start justify-between">
						<view class="topInfo-box-r-name">
							{{userName ? userName : '游客'}}
						</view>
						<view class="topInfo-box-r-txt" v-if="releationId">
							ID：{{releationId}}
						</view>
					</view>
				</view>
			</view>

			<view class="tools" style="padding: 30rpx 0;" @tap="goOrder">
				<view style="padding: 0rpx 30rpx;">
					<view class="flex align-center justify-between">
						<view class="tools_title">我的订单</view>
						<u-icon name="arrow-right" color="#ffffff" size="18"></u-icon>
					</view>
				</view>
				<view class="margin-top"></view>
				<u-line color="#7FFFDE"></u-line>
				<view class="flex align-center justify-between" style="padding: 38rpx 50rpx 0;">
					<view class="text-center">
						<image src="/static/images/my/dfk.png" class="tools_orderImage"></image>
						<view class="text-sm">待支付</view>
					</view>
					<view class="text-center">
						<image src="/static/images/my/dfh.png" class="tools_orderImage"></image>
						<view class="text-sm">待接单</view>
					</view>
					<view class="text-center">
						<image src="/static/images/my/ywc.png" class="tools_orderImage"></image>
						<view class="text-sm">已完成</view>
					</view>
				</view>
			</view>

			<view class="tools flex align-center justify-center" style="padding: 20rpx 0rpx;">
				<view class="tools-box">
					<view @click="goNav(item.url)" class="tools-box-item flex align-center justify-between"
						v-for="(item,index) in toolList" :key="index">
						<view class="tools-box-item-l flex align-center">
							<image :src="item.icon"></image>
							<text>{{item.name}}</text>
						</view>
						<u-icon name="arrow-right" color="#ffffff" size="14"></u-icon>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				leftItem: '',
				titleStyle: {
					color: '#FFFFFF',
					fontSize: '32rpx',
					fontWeight: 'bold'
				},
				releationId: '',
				tokenShow: false,
				avatar: '',
				userName: '',
				toolList: [{
					name: '客服中心',
					icon: '/static/images/my/kf.png',
					url: '/pages/kefu/kefu'
				}, {
					name: '关于我们',
					icon: '/static/images/my/gy.png',
					url: '/pages/member/about'
				}, {
					name: '意见反馈',
					icon: '/static/images/my/yj.png',
					url: '/pages/feedback/index'
				}, {
					name: '优惠券',
					icon: '/static/images/my/yhq.png',
					url: '/pages/coupon/myCompon'
				}, {
					name: '积分兑换',
					icon: '/static/images/my/myjifen.png',
					url: '/my/jifen/index'
				}, {
					name: '设置中心',
					icon: '/static/images/my/seting.png',
					url: '/pages/my/set'
				}],
			};
		},
		onLoad() {

		},
		onShow() {
			let token = this.$queue.getData('token')
			if (token) {
				this.getUserInfo();
				this.tokenShow = true;
			} else {
				this.tokenShow = false;
				this.avatar = '/static/logo.png'
				this.userName = '游客'
				this.releationId = ''
			}
		},
		methods: {
			goOrder(){
				uni.switchTab({
					url:'/pages/order/order'
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
				let userId = this.$queue.getData('userId');
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
						this.releationId = res.data.invitationCode
						this.$queue.setData('avatar', res.data.avatar);
					}
				});
			},
			goNav(url) {
				let token = this.$queue.getData('token')
				if (!token) {
					uni.navigateTo({
						url: '/pages/public/login'
					});
					return
				}
				if (url === '/pages/kefu/kefu') {
					this.goChat();
					return
				}
				uni.navigateTo({
					url
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #1F2A25;
	}

	.bg_view {
		background: linear-gradient(to bottom, #33513e 0%, #295640 20%, #2F302B 60%);
	}

	.topInfo {
		width: 100%;
		height: auto;
		padding: 40rpx 0;

		.topInfo-box {
			width: 686rpx;
			height: 100%;

			image {
				width: 88rpx;
				height: 88rpx;
				border-radius: 50%;
			}
		}

		.topInfo-box-r {
			margin-left: 20rpx;
			height: 88rpx;
			flex-direction: column;
			padding: 10rpx 0;

			.topInfo-box-r-name {
				font-size: 32rpx;
				font-weight: bold;
			}

			.topInfo-box-r-txt {
				color: #A5A6A5;
				font-size: 24rpx;
			}
		}
	}

	.tools {
		width: 686rpx;
		height: auto;
		background: #F2F6FC;
		border-radius: 24rpx;
		border: 2rpx solid #7FFFDE;
		margin: 0 auto;
		margin-top: 20rpx;

		.tools_orderImage {
			width: 52rpx;
			height: 52rpx;
		}

		.tools_title {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 32rpx;
			color: #FFFFFF;
		}

		.tools-box {
			width: 686rpx;
			height: 100%;
		}

		.tools-box-item {
			padding: 24rpx 30rpx;

			.tools-box-item-l {
				image {
					width: 50rpx;
					height: 50rpx;
				}

				text {
					margin-left: 20rpx;
				}
			}
		}

	}
</style>