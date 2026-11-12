<template>
	<view>
		<view class="flex align-center justify-between"
			style="background-color:#FFFFFF;padding: 40rpx 30rpx;">
			<view class="">
				<view>当前可用积分</view>
				<view style="font-size: 50upx;font-weight: bold;flex: 6;">{{ integralNum ? integralNum : 0 }}
				</view>
			</view>
			<view v- @click="golist"
				style="background: #5CC477;color: #FFFFFF;padding: 20upx 30rpx;border-radius: 10upx;text-align: center;">
				积分明细</view>
		</view>

		<view class="margin flex align-center justify-between flex-wrap">
			<view v-for="(item,index) in list" :key="index" class="listbox">
				<image :src="item.coupon && item.coupon.goodsImages?item.coupon.goodsImages:'/static/logo.png'"
					mode="aspectFill"></image>
				<view class="listbox_bot">
					<view class="listbox_bottit text-cut">{{item.couponName}}</view>
					<view class="listbox_botbox">
						<view class="listbox_botleft">{{item.integral}}<text>积分</text></view>
						<view class="listbox_botdh" @click="duihuan(item)">兑换</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length == 0" content="暂无可兑换物品" />
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
				integralNum: 0,
				page: 0,
				limit: 10,
				pages: 1,
				list: []
			}
		},
		onLoad() {

		},
		onShow() {
			this.getUserInfo()
			this.page = 0
			this.getlist()
		},
		methods: {
			// 跳转积分明细
			golist() {
				uni.navigateTo({
					url: '/my/jifen/index'
				})
			},
			// 兑换
			duihuan(item) {
				this.$Request.postT("/app/integral/creditsExchange?couponIssueId=" + item.couponIssueId).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '兑换成功',
							icon: 'none'
						})
						this.getUserInfo()

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
			//获取个人积分
			getUserInfo() {
				this.$Request.getT("/app/integral/selectByUserId").then(res => {
					if (res.code == 0) {
						this.integralNum = res.data.integralNum

					}
				});
			},
			// 兑换列表接口
			getlist() {
				let data = {
					page: this.page,
					size: this.limit,
					type: 2
				}
				this.$Request.getT("/app/selfCouponIssue/useList", data).then(res => {
					if (res.status === 0) {
						this.pages = res.data.totalPages
						if (this.page === 0) {
							this.list = res.data.content;
						} else {
							this.list = [...this.list, ...res.data.content];
						}
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			}
		},
		onReachBottom: function() {
			if (this.pages > this.page + 1) {
				this.page = this.page + 1;
				this.getlist();
			} else {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 0;
			this.getlist();
		}
	}
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

	.listbox {
		width: 338rpx;
		height: 388rpx;
		background: #FFFFFF;
		border: 1px solid #92FDCB;
		border-radius: 16rpx;
		color: #FFFFFF;
		margin-bottom: 20rpx;

		image {
			width: 100%;
			height: 240rpx;
			border-radius: 16rpx 16rpx 0 0;
		}

		.listbox_bot {
			padding: 24rpx;
			color: #333333;

			.listbox_bottit {
				font-family: PingFang SC;
				font-weight: 800;
				font-size: 30rpx;
			}

			.listbox_botbox {
				display: flex;
				align-items: center;
				justify-content: space-between;
				margin-top: 10rpx;

				.listbox_botleft {
					font-family: DINPro;
					font-weight: bold;
					font-size: 38rpx;

					text {
						font-size: 24rpx;
						font-weight: 500;
					}
				}

				.listbox_botdh {
					width: 96rpx;
					height: 48rpx;
					background: #5CC477;
					border-radius: 24rpx;
					font-family: PingFang SC;
					font-weight: bold;
					font-size: 24rpx;
					color: #FFFFFF;
					display: flex;
					align-items: center;
					justify-content: center;
				}
			}
		}



	}
</style>