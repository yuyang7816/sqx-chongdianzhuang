<template>
	<view style="padding-bottom: 40rpx;">
		<u-sticky>
			<view class="tabs">
				<u-tabs lineWidth="32rpx" lineHeight="10rpx" :lineColor="`url(${lineBg}) 100% 100%`" lineBgSize="cover"
					:list="tabsList" :current="tabCurrent" :scrollable="false" @click="clickTabs"
					:activeStyle="activeStyle" :inactiveStyle="inactiveStyle"></u-tabs>
			</view>
		</u-sticky>
		<view class="orderList flex align-center justify-center" v-if="list.length > 0">
			<view class="orderList-box">
				<view class="orderList-box-item" @click="goNav('/pages/order/info?id=' + item.ordersNo)"
					v-for="(item,index) in list" :key="index">
					<view class="orderList-box-item-status flex align-center justify-between">
						<view class="orderList-box-item-status-l flex align-center">
							<!-- <view class="orderList-box-item-status-l-i" v-if="item.status && item.status != 1 && item.status != 5">
								<image src="/static/images/order/timeIcon.png" mode=""></image>
								88分钟
							</view> -->
							<view class="orderList-box-item-status-l-i">
								<image src="/static/images/order/moneyIcon.png" mode=""></image>
								￥{{item.payMoney ? item.payMoney : 0}}
							</view>
						</view>
						<!-- 状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价 7待补费 -->
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 1">
							待支付
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 2">
							待使用
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 3">
							使用中
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 4">
							已完成
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 5">
							已取消
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 6">
							待评价
						</view>
						<view class="orderList-box-item-status-r" v-if="item.status && item.status == 7">
							待补费
						</view>
					</view>
					<view class="orderList-box-item-info">
						<view class="orderList-box-item-info-i">
							订单编号：{{item.ordersNo}}
						</view>
						<view class="orderList-box-item-info-is" v-if="item.payTime">
							下单时间：{{item.payTime}}
						</view>
						<view class="orderList-box-item-info-is">
							下单地点：{{item.tearoomName}}
						</view>
						<view class="orderList-box-item-info-is">
							柜子名称：{{item.goodsName}}
						</view>
					</view>
					<view class="orderList-box-item-btn flex align-center justify-end">
						<view class="orderList-box-item-btn-i flex align-center justify-center"
							v-if="item.status && item.status == 3">
							去归还
						</view>
						<view class="orderList-box-item-btn-i flex align-center justify-center"
							v-if="item.status && item.status == 1" @tap.stop="closeOrder(item)">
							取消订单
						</view>
						<view class="orderList-box-item-btn-i flex align-center justify-center"
							v-if="item.status && item.status == 1">
							去支付
						</view>
					</view>


				</view>
			</view>
		</view>
		<empty content="暂无订单" v-if="list.length == 0" />
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
				lineBg: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACAAAAAKCAYAAADVTVykAAAAAXNSR0IArs4c6QAAAulJREFUOE+lk0+IXEUQxqu6ut/M7Ozsi0Q3IeA/VLI5hCWgePYQjV7FiODFg5CTst68zcmLYo6CkAQVFKN40IMeDHoxIB4kGvHgP9BEkrgka3Y3O/Oqqj/t9zKLAXPRB0031V39/ep71fwETsoSHViqCC8K/JCQh4D8cS/kV3bT8jeHmZ3+5weM+5c2r+4Vyk9F9ocjuQnlj8D5DR7jx0UmjIXwdCAbCWWKsC2hfDpwPuq0/sUKP7T2Xxh+w8pA1+YW+wM7JG7PJMpLQjoScorkFwV4i8f4ZYnJPhTy+4q4oGwaCblH+O+S7YMIf3su+c+rtLX+HD82vSkMiE+fW+nvrOth6NFiRH5Esj2eyPYJ7JaEzMLWahQIoXy+OHCvEN4P8OUu2I0CEdGCbFWkFyL0S8n6eS/5V4kmq31y081JHnHDzlOZD1ZVSHcFnu6PuXkwst0fYbclaB3JuL0TVirf1gjwizzGDwuRcCTAXxDyXTcClCSlRFoSNUHXI3QzsW0k6BUh3eyRSkJTJ3gt0GFiHUTYMJGmCKeE5rpoEW8Luu6yrwbO73Cx8yV8v5OIno3wI0J2e2nEjrYbCS1AO5chXOY2jkTKBbA9U/a5g67anJLf5c7uC/CpIP8aQ34z8vREC1C+Mc7ODwkHA/LzkfRAgi1I+xvK5U6Ry7qhahYrotv7ShUpSQtirWNlnolLC2eTxPlSyP4Jix8fbFz5jhff29gG6CA+i3M0urOf8WQkOxzJ7ojQhQSTDqAIzeauwmJx60zbN9161j+JmtLI6wL9I3H+FFnfDbn5tq6PXZ4VfgPALPgyzgxHtLUn5MnBCv5oZN+f0MwLdK4irxIaKZaWqtv+aF0yJDYTNE0FvxZZNyL0rGQ/VWF6Krier+vX1vjvN//PV/SvALMDr+LkYAfturVnk7uroA8E1+WK7Z6IZndi2xGz9nqklmBXI03/TMgXhPSnxHYm5ObrRHouDNYu7+HXr93s6f4FxMrEKTVot/MAAAAASUVORK5CYII=',
				activeStyle: {
					color: '#7EF9CD'
				},
				inactiveStyle: {
					color: '#CCCCCC'
				},
				tabCurrent: 0,
				tabsList: [{
						name: '全部',
						status: ''
					},
					{
						name: '待支付',
						status: 1
					},
					{
						name: '租赁中',
						status: 2
					},
					{
						name: '已归还',
						status: 3
					}
				],
				status: '',
				page: 1,
				pages: 1,
				limit: 10,
				list: [],
			};
		},
		onLoad(d) {
			this.getList();
		},
		onShow() {
			let token = this.$queue.getData('userId');
			if (token) {
				this.page = 1;
				this.getList();
			} else {
				this.list = []
			}
		},
		methods: {
			clickTabs(e) {
				console.log(e)
				this.status = e.status
				this.page = 1
				this.getList()
			},
			closeOrder(item) {
				let that = this
				uni.showModal({
					title: '温馨提示',
					content: '确认取消当前订单吗？',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认',
					success: rer => {
						if (rer.confirm) {
							let data = {
								ordersNo: item.ordersNo
							}
							that.$queue.showLoading('取消中...')
							that.$Request.postT('/app/orders/cancelOrders', data).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									that.page = 1
									that.getList()
								} else {
									that.$queue.showToast(res.msg);
								}
							})
						}
					}
				});
			},
			getList() {
				this.$queue.showLoading('加载中...')
				this.$Request.getT('/app/orders/selectOrdersList?page=' + this.page + '&limit=' + this.limit + '&status=' +
						this.status)
					.then(res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.list = []
							}
							res.data.list.forEach(d => {
								this.list.push(d);
							});
						}
						uni.hideLoading();
						uni.stopPullDownRefresh();
					});
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
		},
		onReachBottom() {
			this.page += 1
			this.getList();
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList();
		}
	}
</script>

<style lang="scss">
	page {
		background: #1F2A25;
	}

	.tabs {
		background-color: #1F2A25;
		padding: 20rpx 0;
	}

	.orderList {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.orderList-box {
			width: 686rpx;
			height: 100%;
		}

		.orderList-box-item {
			width: 100%;
			// padding: 32rpx;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border: 1px solid #92FDCB;
			margin-bottom: 20rpx;
			border-radius: 24rpx;
		}

		.orderList-box-item-status {
			padding: 32rpx;
			border-bottom: 1px solid #7FFFDE;

			.orderList-box-item-status-l-i {
				display: flex;
				align-items: center;
				margin-right: 48rpx;

				image {
					width: 38rpx;
					height: 38rpx;
					margin-right: 8rpx;
				}

				font-size: 30rpx;
				font-weight: bold;
			}

			.orderList-box-item-status-r {
				color: #7EF9CD;
				font-weight: bold;
			}
		}

		.orderList-box-item-info {
			padding: 32rpx 32rpx 32rpx 32rpx;

			.orderList-box-item-info-i {
				font-size: 30rpx;
				font-weight: bold;
			}

			.orderList-box-item-info-is {
				color: #FFFFFF;
				font-weight: 500;
				margin-top: 20rpx;
			}
		}

		.orderList-box-item-btn {
			padding: 0rpx 32rpx 32rpx 32rpx;

			.orderList-box-item-btn-i {
				width: 150rpx;
				height: 60rpx;
				background: linear-gradient(90deg, #7FFFDE 0%, #FDF361 100%);
				border-radius: 30rpx;
				margin-left: 20rpx;
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #1A1A1A;
			}
		}
	}
</style>