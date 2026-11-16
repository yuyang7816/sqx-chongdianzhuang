<template>
	<view style="padding-bottom: 40rpx;">
		<u-sticky>
			<view class="tabs">
				<u-tabs lineColor="#59D277" :scrollable="false" :list="tabs" @click="clickTab"></u-tabs>
			</view>
		</u-sticky>
		<view class="list flex align-center justify-center" v-for="(item,index) in list">
			<view class="list-box">
				<view class="list-box-info">
					<view class="list-box-top flex align-center justify-between">
						<view class="list-box-money flex align-center justify-center">
							<view class="list-box-money-price">
								<text class="money-unit">￥</text>{{item.lessMoney}}
							</view>
							<view class="list-box-money-condition">
								满{{item.minMoney}}可用
							</view>
						</view>
						<view class="list-box-divider"></view>
						<view class="list-box-main flex justify-between">
							<view class="list-box-main-info">
								<view class="list-box-info-l-title">
									{{item.couponName}}
								</view>
								<view class="list-box-info-l-time">
									有效期至{{formatDate(item.failureTime)}}
								</view>
								<view v-if="item.tearoom && item.tearoom.tearoomName" class="list-box-info-l-shop">
									适用门店:{{item.tearoom.tearoomName}}
								</view>
								<view v-if="item.teamRule" @click="showGZ(item)"
									class="list-box-rule flex align-center">
									使用规则
									<view class="list-box-rule-icon">
										<u-icon name="arrow-down" color="#58B530" size="16"></u-icon>
									</view>
								</view>

							</view>
							<view class="list-box-main-action" v-if="item.status == 1" @tap="goHome">
								<view class="list-box-info-r flex align-center justify-center">
									使用
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
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
		<empty content="暂无优惠券" v-if="list.length == 0" />
	</view>
</template>

<script>
	import empty from '@/components/empty.vue';
	import config from '@/common/config.js';
	export default {
		components: {
			empty
		},
		data() {
			return {
				teamRule: '',
				showXz: false,

				tabs: [{
						name: '未使用',
						type: 1
					},
					{
						name: '已使用',
						type: 2
					},
					{
						name: '已过期',
						type: 3
					}
				],
				list: [],
				page: 0,
				pages: 0,
				size: 10,
				couponType: 1
			};

		},
		onLoad(option) {
			this.getUseList()
		},
		onPullDownRefresh() {
			this.page = 0
			this.getUseList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.getUseList()
			}
		},
		methods: {
			formatDate(value) {
				if (!value) {
					return '';
				}
				return String(value).split(' ')[0];
			},
			goHome() {
				uni.switchTab({
					url: '/pages/index/index'
				});
			},
			showGZ(item) {
				this.teamRule = item.teamRule;
				console.log(this.teamRule)
				this.showXz = true;
			},
			closeXz() {
				this.showXz = !this.showXz
			},
			clickTab(e) {
				console.log(e)
				this.couponType = e.type
				this.page = 0
				this.getUseList()
			},
			//获取优惠券列表
			getUseList() {
				let data = {
					status: this.couponType,
					page: this.page,
					size: this.size
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.status == 0) {
						if (res.data.totalPages > 0) {
							this.pages = res.data.totalPages - 1
						}
						if (this.page == 0) {
							this.list = res.data.content
						} else {
							this.list = this.list.concat(res.data.content)
						}
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	.tabs {
		width: 100%;
		background-color: #FFFFFF;
		padding: 10rpx 0;
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.list-box {
			width: 686rpx;
			// height: 256rpx;
			position: relative;
		}

		.list-box-info {
			width: 100%;
			height: 100%;
			background: #FFFFFF;
			border: 2rpx solid #DCEFD9;
			border-radius: 28rpx;
			box-shadow: 0 12rpx 32rpx rgba(88, 181, 48, 0.08);
			// padding: 24rpx 28rpx 22rpx;
			padding: 40rpx 30rpx;
			box-sizing: border-box;
			position: relative;
			overflow: hidden;

			.list-box-top {
				height: 118rpx;
				align-items: center;
			}

			.list-box-money {
				width: 180rpx;
				height: 100%;
				flex-direction: column;
				color: #58B530;
				flex-shrink: 0;
				justify-content: center;
			}

			.list-box-money-price {
				font-size: 52rpx;
				line-height: 1;
				font-weight: bold;

				.money-unit {
					font-size: 22rpx;
					margin-right: 4rpx;
					vertical-align: middle;
				}
			}

			.list-box-money-condition {
				font-size: 24rpx;
				margin-top: 8rpx;
				color: #58B530;
				line-height: 1.2;
			}

			.list-box-divider {
				width: 2rpx;
				height: 92rpx;
				background: linear-gradient(180deg, rgba(88, 181, 48, 0.15) 0%, rgba(88, 181, 48, 0.45) 50%, rgba(88, 181, 48, 0.15) 100%);
				margin: 0 20rpx 0 12rpx;
				flex-shrink: 0;
			}

			.list-box-main {
				flex: 1;
				align-items: center;
				height: 100%;
			}

			.list-box-main-info {
				flex: 1;
				padding-right: 12rpx;
				min-width: 0;
			}

			.list-box-info-l-title {
				color: #1F2A1F;
				font-size: 30rpx;
				font-weight: bold;
				line-height: 1.2;
			}

			.list-box-info-l-time {
				color: #7B847B;
				font-size: 22rpx;
				margin-top: 12rpx;
				line-height: 1.3;
			}

			.list-box-info-l-shop {
				color: #666666;
				font-size: 22rpx;
				margin-top: 10rpx;
				line-height: 1.2;
			}

			.list-box-rule {
				margin-top: 10rpx;
				color: #58B530;
				font-size: 22rpx;
				line-height: 1.2;
			}

			.list-box-main-action {
				flex-shrink: 0;
			}

			.list-box-rule-icon {
				margin-left: 10rpx;
			}

			.list-box-info-r {
				background: linear-gradient(135deg, #67C93F 0%, #58B530 100%);
				color: #FFFFFF;
				font-size: 24rpx;
				font-weight: bold;
				width: 124rpx;
				height: 54rpx;
				border-radius: 27rpx;
				box-shadow: 0 10rpx 20rpx rgba(88, 181, 48, 0.18);
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