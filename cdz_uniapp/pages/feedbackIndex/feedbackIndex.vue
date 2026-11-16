<!-- 帮助反馈页面 -->
<template>
	<view class="container" style="padding-bottom: 140rpx;">
		<!-- <view class="text-top">常见问题</view> -->

		<view class="bg-list filter">
			<view v-for="(item,index) in helpClassifyList" :key="index" :title="item.helpClassifyName"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="openList(item)">
					<view class="text-title">{{item.helpClassifyName}}</view>
					<view @click.stop="openList(item)">
						<image src="../../static/ups.png" style="width: 21rpx;height: 15rpx;" v-if="item.parentId==0">
						</image>
						<image src="../../static/dowm.png" style="width: 21rpx;height: 15rpx;" v-else></image>
					</view>
				</view>
				<view v-for="(problemItem,problemIndex) in item.helpWordList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)" v-if="item.parentId==0">
					<view class="text-item">{{problemItem.helpWordTitle}}</view>
					<view class="line" v-if="problemIndex!=item.helpWordList.length-1"></view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				helpClassifyList: [],
				SelKeFu: '1'
			}
		},
		onLoad() {
			this.SelKeFu = this.$queue.getData('SelKeFu')
			this.getlist()
		},
		methods: {
			openList(item) {
				var oldhelpClassifyList = item
				if (oldhelpClassifyList.parentId == 1) {
					item.parentId = 0
				} else {
					item.parentId = 1
				}
			},
			getlist() {
				this.$Request.get("/app/help/selectHelpList ").then(res => {
					if (res.code == 0) {
						this.helpClassifyList = res.data
						for (var i = 0; i < this.helpClassifyList.length; i++) {
							this.helpClassifyList[i].parentId = 1
						}
						// this.helpClassifyList.isTrue = false

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			onClick(item) {
				uni.navigateTo({
					url: '/my/helpDetail/helpDetail?helpWordId=' + item.helpWordId
				})

			},
			toFeedbackList() {
				let that = this
				let kefu = this.$queue.getData('SelKeFu'); // 用户端联系方式 1 手机号 2企业微信 3 客服二维码  
				let kefuPhone = this.$queue.getData('kefuPhone');
				if (kefu == 1) {
					uni.makePhoneCall({
						phoneNumber: kefuPhone //仅为示例
					});
				} else if (kefu == 4) { //客服二维码
					uni.navigateTo({
						url: '/my/setting/customer'
					});
				} else if (kefu == 2) {
					// #ifdef MP-WEIXIN
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
					let url = that.$queue.getData('SelKeFuLink');
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

				} else if (kefu + '' == '3') { //微信小程序客服  H5跳转二维码客服
					// #ifndef MP-WEIXIN
					uni.navigateTo({
						url: '/my/setting/customer'
					})
					// #endif
				}

			},

			toFeedback() {
				uni.navigateTo({
					url: '/my/feedback/index',
					success: res => {},
					fail: () => {},
					complete: () => {}
				});
			}
		}
	}
</script>

<style>
	page{
		background: #FFFFFF;
	}
	button::after {
		border: none;
		background-color: none;
	}

	button {
		display: flex;
		align-items: center;
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		// line-height: 96rpx;
		overflow: hidden;
		color: #FFFFFF;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		font-size: 32rpx;
		width: 100%;
		height: 100%;
		color: #333333;
	}



	.bg-box {
		background-color: #FFFFFF;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		border: none !important;
	}

	.bg-list {
		margin-bottom: 100rpx;
		/* background-color: #FFFFFF; */
		padding: 30rpx
	}

	.bg-white-box {
		/* background-color: #F7F7F7; */
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;
		color: #000;
		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #d3d3d3;
	}



	.text-title {
		
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {
		color: #999999;
		font-size: 28rpx;
		padding: 24rpx 0rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {
		color: #000;
		font-size: 28rpx;
	}

	.hover {
		/* background-color: #ffffff; */
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
		color: #ffffff;
		/* color: #000000; */
	}

	.text-top {
		margin: 30rpx;
		color: #000;
		font-size: 34rpx;
	}
</style>