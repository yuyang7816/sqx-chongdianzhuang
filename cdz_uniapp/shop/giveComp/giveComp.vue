<template>
	<view>
		<view class="listbox" v-for="(item,index) in list" :key="index">
			<view class="flex align-start ">
				<view style="width: 100%;">
					<view class="flex align-center justify-between"
						style="color: #000000;font-size: 30upx;width: 100%;">
						{{item.couponName}}
						<!-- <view style="color: #FDA310;">
							库存：{{item.remainNumber?item.remainNumber:0}}
						</view> -->
					</view>
					<view style="color: #999999;font-size: 24upx;margin-top: 10upx;"
						v-if="item.validDay && item.validDay>0">
						有效期{{item.validDay}}天</view>
					<view style="color: #999999;font-size: 24upx;margin-top: 10upx;" v-else>永久有效</view>
				</view>
				<!-- <view style="color: #FD3C44;font-size:28upx;" v-if="item.hour">
					限制{{item.hour}}小时使用
				</view> -->
			</view>
			<view class="margin-top-sm flex align-center flex-wrap" v-if="item.tearoom&&item.tearoom.tearoomName">
				仅限【{{item.tearoom.tearoomName}}
				<text style="margin: 0 8rpx;">—</text>
				<view v-for="(ite,ind) in item.goodsList" :key="ind"> {{ite.goodsName?ite.goodsName:''}}
					<text v-if="item.goodsList.length-1>ind">、</text>
				</view>
				】使用
			</view>
			<view style="width: 100%;border-top:1rpx dashed #E6E6E6;margin: 30upx 0upx;"></view>
			<view class="flex align-center justify-between">
				<!-- <view v-if="item.old_money">满{{item.old_money}}元可用</view>
				<view v-else>无门槛使用</view> -->
				<!-- <view class="">
					<view style="color: #32BE62;font-size:28upx;" v-if="item.hour">
						限制{{item.hour}}小时使用
					</view>
				</view> -->
				<view class="">

				</view>
				<view class="btn" @click="giveItem(item)" v-if="item.status==1">立即赠送</view>
			</view>
		</view>
		<empty v-if="list.length == 0"></empty>
		<!-- 选择用户赠送 -->
		<u-popup zIndex="99" round="24" :show="show" @close="close" closeable>
			<view class="userListPopo">
				<view class="userListPopo-title">
					选择用户
				</view>
				<view class="userListPopo-search">
					<u-search placeholder="请输入手机号搜索" :showAction="false" v-model="phone" @search="search"
						@clear="search"></u-search>
				</view>
				<view class="userListPopo-list">
					<scroll-view scroll-y="true" style="width: 100%;height: 100%;">
						<view class="userListPopo-list-item" v-for="(item,index) in userList" :key="index">
							<view class="userListPopo-list-itemt flex align-center justify-between">
								<view class="flex align-center">
									<image :src="item.avatar?item.avatar:'/static/logo.png'" mode="aspectFill"></image>
									<view class="userListPopo-list-itemt-r">
										<view class="userListPopo-list-itemt-r-name">
											{{item.userName?item.userName:'匿名'}}
										</view>
										<view class="userListPopo-list-itemt-r-phone">
											{{item.phone?item.phone:'暂无手机号'}}
										</view>
									</view>
								</view>
								<view @click="giveInfos(item)" class="userListPopo-list-itemb">
									赠送
								</view>
							</view>

						</view>
					</scroll-view>
					<empty height="0" top="100rpx" v-if="userList.length == 0" content="暂无用户" />
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import empty from '@/components/empty'
	export default {
		components: {
			empty
		},
		data() {
			return {
				tearoomId: '', //门店id
				page: 1,
				pages: 1,
				limit: 10,
				list: [],
				compInfo: {}, //临时存储选择的优惠券信息
				show: false, //是否显示用户列表
				phone: '',
				userList: [], //用户列表
			};
		},
		onLoad(option) {
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId
				uni.showLoading({
					title: '加载中...'
				})
				this.getCompList()
				this.getUserList();
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getCompList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.getCompList()
			}
		},
		methods: {
			giveInfos(item) {
				uni.showModal({
					title: '提示',
					content: '确认赠送吗？',
					complete: (ret) => {
						if (ret.confirm) {
							let data = {
								userIds: item.userId,
								couponIds: this.compInfo.couponIssueId
							}
							uni.showLoading({
								title: '赠送中...'
							})
							this.$Request.postT('/app/selfCouponUser/sendUserCoupon', data).then(res => {
								uni.hideLoading()
								if (res.status == 0) {
									uni.showToast({
										title: '已赠送'
									})
									this.close();
									this.page = 1
									this.getCompList()
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
			getUserList() {
				let data = {
					page: 1,
					limit: 99,
					phone: this.phone
				}
				this.$Request.getT('/app/user/selectCleanKeepingUserList', data).then(res => {
					if (res.code == 0) {
						this.userList = res.data.list
					}
				})
			},
			//搜索
			search() {
				this.getUserList()
			},
			close() {
				this.show = false
				this.phone = ''
			},
			//赠送
			giveItem(item) {
				this.compInfo = item
				this.getUserList()
				this.show = true
			},
			getCompList() {
				let data = {
					page: this.page - 1,
					size: this.limit,
					tearoomId: this.tearoomId,
					// type: 1
				}
				this.$Request.getT('/app/selfCouponIssue/list', data).then(res => {
					uni.stopPullDownRefresh()
					uni.hideLoading()
					if (res.status == 0) {
						this.pages = res.data.totalPages
						if (this.page == 1) {
							this.list = res.data.content
						} else {
							this.list = [...this.list, ...res.data.content]
						}
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	.userListPopo {
		width: 100%;
		height: auto;
		padding: 32rpx;

		.userListPopo-title {
			font-size: 32rpx;
			font-weight: bold;
		}

		.userListPopo-search {
			margin-top: 20rpx;
		}

		.userListPopo-list {
			height: 500rpx;
			margin-top: 20rpx;

			.userListPopo-list-item {
				margin-bottom: 20rpx;

				.userListPopo-list-itemt {
					image {
						width: 100rpx;
						height: 100rpx;
						border-radius: 50%;
						margin-right: 20rpx;
					}
				}

				.userListPopo-list-itemt-r-name {
					font-size: 30rpx;
					font-weight: bold;
				}

				.userListPopo-list-itemt-r-phone {
					margin-top: 10rpx;
					font-size: 30rpx;
					// font-weight: bold;
				}

				.userListPopo-list-itemb {
					width: 150rpx;
					height: 60rpx;
					background-color: #32BE62;
					color: #FFFFFF;
					border-radius: 40rpx;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}
		}
	}

	.listbox {
		margin: 30upx;
		background: #FFFFFF;
		border-radius: 8upx;
		padding: 30upx;
	}

	.btn {
		color: #32BE62;
		border: 1rpx solid #32BE62;
		border-radius: 55upx;
		padding: 8upx 25upx;
	}
</style>