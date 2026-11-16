<template>
	<view class="padding-bottom-xl">
		<u-sticky customNavHeight="0">
			<view class="headtop">
				<u-tabs :list="tablist" :current="current" :scrollable="true" @click="click" :activeStyle="activeStyle"
					:inactiveStyle="inactiveStyle" lineColor="#32BE62"></u-tabs>
			</view>
		</u-sticky>
		<view class="listItem" v-for="(item,index) in list" v-if="list.length > 0">
			<!-- <view class="flex justify-between padding">
				<view class="listItem_title"></view>
				<view class="listItem_state">{{item.statusName}}</view>
			</view> -->
			<!-- <u-line direction="row" color="#E6E6E6" border-style="dashed" /> -->
			<view class="flex padding">
				<image :src="item.tearoomTitleImage?item.tearoomTitleImage:'../../static/logo.png'"
					class="listItem_img">
				</image>
				<view class="listItem_right">
					<view class="right_bxname flex align-center justify-between">
						{{item.goodsName}}
						<view class="listItem_state">{{item.statusName}}</view>
					</view>
					<view class="right_bxcrat">开始时间：{{item.startTime?item.startTime:'暂无'}}</view>
					<!-- <view class="right_bxmoney">￥<text>20</text></view> -->
				</view>
			</view>
			<!-- <u-line direction="row" color="#E6E6E6" border-style="dashed" /> -->
			<view class="flex align-center justify-between padding-lr">
				<view class="right_bxmoney">实付：<text>￥{{item.payMoney?item.payMoney:0}}</text></view>
				<view class="flex align-center">
					<!-- <view class="btn1 flex align-center justify-center" v-if="item.status == 2"
						@click="closeOrder(item.ordersNo)">取消订单</view> -->
					<view class="btn flex align-center justify-center" @tap="goDetail(item.ordersNo)">查看详情</view>
				</view>
			</view>
		</view>
		<u-loadmore v-if="list.length>0" :status="status" marginTop="20" />
		<empty v-if="list.length==0" />
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				status: 'loadmore',
				list: [],
				tablist: [{
					name: '全部',
					status: 0
				}, {
					name: '待支付',
					status: 1
				}, {
					name: '待使用',
					status: 2
				}, {
					name: '使用中',
					status: 3
				}, {
					name: '已结束',
					status: 4
				}, {
					name: '已取消',
					status: 5
				}, {
					name: '待评价',
					status: 6
				}],
				current: 0,
				activeStyle: {
					color: '#442916'
				},
				inactiveStyle: {
					color: '#999999'
				},
				tearoomId: '', //当前棋牌室id
				page: 1,
				limit: 10,
				pages: 1
			}
		},
		onLoad(d) {
			if (d.index) {
				this.current = d.index;
			}
			if (d.tearoomId) {
				this.tearoomId = d.tearoomId
				this.getOrderList()
			}
		},
		onShow() {
			if (this.tearoomId) {
				this.page = 1
				this.getOrderList()
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getOrderList()
		},
		onReachBottom() {
			if (this.page < this.pages) {
				this.page += 1
				this.status = 'loading'
				this.getOrderList()
			}
		},
		methods: {
			//取消订单
			closeOrder(ordersNo) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认取消订单吗？',
					complete(ret) {
						if (ret.confirm) {
							uni.showLoading({
								title: '取消中...'
							})
							let data = {
								ordersNo: ordersNo
							}
							that.$Request.postT('/app/orders/cancelOrders', data).then(res => {
								if (res.code == 0) {
									uni.hideLoading()
									uni.showToast({
										title: '已取消'
									})
									that.page = 1
									that.getOrderList()
								} else {
									uni.hideLoading()
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
			//获取订单列表
			getOrderList() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: this.tablist[this.current].status,
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/orders/selectOrdersLists', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						res.data.list.map(item => {
							if (item.status == 1) {
								item.statusName = '待支付'
							} else if (item.status == 2) {
								item.statusName = '待使用'
							} else if (item.status == 3) {
								item.statusName = '使用中'
							} else if (item.status == 4) {
								item.statusName = '已完成'
							} else if (item.status == 5) {
								item.statusName = '已取消'
							} else if (item.status == 6) {
								item.statusName = '待评价'
							}
						})
						this.pages = res.data.totalPage //总页数
						if (this.page < this.pages) {
							this.status = 'loadmore'
						} else {
							this.status = 'nomore'
						}
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//跳转详情
			goDetail(ordersNo) {
				uni.navigateTo({
					url: '/shop/bxOrder/bxOrderDetail?ordersNo=' + ordersNo
				});
			},
			click(item) {
				this.current = item.index
				this.getOrderList()
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F7F7F7;
	}

	.listItem {
		width: 686rpx;
		// height: 88rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 0 auto;
		margin-top: 20rpx;
		padding-bottom: 30rpx;

		.listItem_title {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: 800;
			color: #333333;
		}

		.listItem_state {
			font-size: 28rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #32BE62;
		}


		.btn {
			width: 150rpx;
			height: 60rpx;
			background: #32BE62;
			border-radius: 30rpx;
			margin-left: 30rpx;
			text-align: center;
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #FFFFFF;
		}

		.btn1 {
			width: 150rpx;
			height: 60rpx;
			border: 1px solid #01993A;
			border-radius: 30rpx;
			margin-left: 30rpx;
			text-align: center;
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #01993A;
		}

		.listItem_img {
			width: 100rpx;
			height: 100rpx;
			border-radius: 24rpx;
		}

		.right_bxmoney {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
			margin-top: 10rpx;

			text {
				font-size: 32rpx;
				color: #FF1536;
			}
		}

		.listItem_right {
			flex: 1;
			// width: 430rpx;
			margin-left: 34rpx;

			.right_bxname {
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: 800;
				color: #333333;
			}

			.right_bxcrat {
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #999999;
				margin-top: 10rpx;

				text {
					color: #333333;
				}
			}
		}
	}

	.headtop {
		padding: 0rpx 20rpx;
		background: #FFFFFF;
	}
</style>