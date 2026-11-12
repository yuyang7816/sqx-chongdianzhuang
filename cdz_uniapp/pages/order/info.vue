<template>
	<view v-if="orderInfo.ordersNo">
		<view class="bg_view">
			<view class="orderStatus flex align-center justify-center">
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 1">
					待支付...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 2">
					待使用...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 3">
					使用中...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 4">
					已完成...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 5">
					已取消...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 6">
					待评价...
				</view>
				<view class="orderStatus-box" v-if="orderInfo.status && orderInfo.status == 7">
					待补费...
				</view>

			</view>
			<view class="orderTime flex align-center justify-center">
				<view class="orderTime-box flex align-center">
					<view class="orderTime-box-item">
						<view class="orderTime-box-item-t">
							<text>{{nowUseTime ? nowUseTime : 0}}</text>
						</view>
						<view class="orderTime-box-item-b">
							时长
						</view>
					</view>
					<view class="orderTime-box-item">
						<view class="orderTime-box-item-t">
							<text>{{orderInfo.payMoney}}</text>元
						</view>
						<view class="orderTime-box-item-b">
							金额
						</view>
					</view>
				</view>
			</view>
			<view class="orderInfo flex align-center justify-center">
				<view class="orderInfo-box">
					<view class="orderInfo-box-title">
						订单信息
					</view>
					<view class="orderInfo-box-item flex align-center justify-between">
						订单号码
						<view class="flex align-center" @tap="copyOrderNum(orderInfo.ordersNo)">
							<text>{{orderInfo.ordersNo}}</text>
							<u-icon name="order" color="#FFFFFF" size="16"></u-icon>
						</view>
					</view>
					<view class="orderInfo-box-item flex align-center justify-between" v-if="orderInfo.payTime">
						下单时间
						<text>{{orderInfo.payTime}}</text>
					</view>
					<view class="orderInfo-box-item flex align-center justify-between"
						v-if="orderInfo.tearoom && orderInfo.tearoom.tearoomName">
						下单地点
						<text>{{orderInfo.tearoom.tearoomName}}</text>
					</view>
					<view class="orderInfo-box-item flex align-center justify-between" v-if="orderInfo.couponName">
						使用优惠券
						<text>{{orderInfo.couponName}}</text>
					</view>
					<view class="orderInfo-box-item flex align-center justify-between" v-if="orderInfo.cashPledge">
						押金金额
						<text>{{orderInfo.cashPledge}}元</text>
					</view>

					<view class="orderInfo-box-item flex align-center justify-between"
						v-if="orderInfo.status && orderInfo.status != 1&& orderInfo.status != 5">
						支付类型
						<text>微信支付</text>
					</view>
				</view>
			</view>
			<view class="orderBtn flex align-center justify-center">
				<view class="flex justify-between" style="width: 686rpx;margin: 0 auto;"
					v-if="orderInfo.status && orderInfo.status == 3">
					<view class="orderBtn-boxs flex align-center justify-center"
						v-if="orderInfo.status && orderInfo.status == 3" @tap="openD">
						打开柜门
					</view>
					<view class="orderBtn-boxs flex align-center justify-center" @tap="closeD">
						结束租赁
					</view>
				</view>

				<view class="flex justify-between" style="width: 686rpx;margin: 0 auto;"
					v-if="orderInfo.status && orderInfo.status == 1">
					<view class="orderBtn-boxs flex align-center justify-center" @tap="closeOrder(orderInfo.ordersNo)">
						取消订单
					</view>
					<view class="orderBtn-boxs flex align-center justify-center" @tap="goPay(orderInfo.ordersNo)">
						立即支付
					</view>
				</view>
				<!-- <view class="orderBtn-box flex align-center justify-center" v-if="orderInfo.status && orderInfo.status == 1"
				@tap="goPay(orderInfo.ordersNo)">
				立即支付
			</view> -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				id: '',
				orderInfo: {
					ordersNo: ''
				},
				nowUseTime: '', //使用中的订单当前使用的时间
				myUseTime: '', //结束的订单使用了多长时间
				nowInter: null
			};
		},
		onLoad(d) {
			if (d.id) {
				this.id = d.id;
			}

			this.getDetail();
		},
		methods: {
			copyOrderNum(ordersNo) {
				uni.setClipboardData({
					data: ordersNo,
					success: r => {
						this.$queue.showToast('复制成功');
					}
				});
			},
			closeOrder(ordersNo) {
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
								ordersNo: ordersNo
							}
							that.$queue.showLoading('取消中...')
							that.$Request.postT('/app/orders/cancelOrders', data).then(res => {
								uni.hideLoading();
								if (res.code == 0) {
									that.$queue.showToast('取消成功！')
									setTimeout(d => {
										that.getDetail();
									}, 2000);
								} else {
									that.$queue.showToast(res.msg);
								}
							})
						}
					}
				});
			},
			openD() {
				this.$queue.showLoading('打开中...')
				this.$Request.getT('/app/room/lightControl?goodsId=' + this.orderInfo.goodsId + '&status=1').then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						this.$queue.showToast('柜门已打开，请放置杆子！')
					} else {
						this.$queue.showToast(res.msg)
					}
				});
			},
			closeD() {
				let that = this;
				uni.showModal({
					title: '温馨提示',
					content: '确认杆子已放置在柜子中并关门结束租赁吗？',
					showCancel: true,
					cancelText: '取消',
					confirmText: '确认',
					success: red => {
						if (red.confirm) {
							that.$queue.showLoading('提交中...')
							if (that.orderInfo.sumMoney && that.orderInfo.sumMoney < 0) {
								that.$Request.postT('/app/wxPay/payCloseOrders?classify=3&ordersNo=' + that
									.orderInfo.ordersNo).then(res => {
									uni.hideLoading();
									if (res.code == 0) {
										uni.requestPayment({
											provider: 'wxpay',
											timeStamp: res.data.timestamp,
											nonceStr: res.data.noncestr,
											package: res.data.package,
											signType: res.data.signType,
											paySign: res.data.sign,
											success: function(suc) {
												uni.hideLoading()
												that.$queue.showToast('支付成功，已结束租赁！')
												setTimeout(d => {
													that.getDetail();
												}, 2000);
											},
											fail: function(err) {
												uni.hideLoading()
												uni.showToast({
													title: '支付失败',
													icon: 'none'
												})
											}
										});
									} else {
										that.$queue.showToast(res.msg);
									}
								});
							} else {
								that.$Request.postT('/app/orders/closeOrders?ordersId=' + that.orderInfo
									.ordersId).then(res => {
									uni.hideLoading();
									if (res.code == 0) {
										that.$queue.showToast('租赁已结束！')
										setTimeout(d => {
											that.getDetail();
										}, 2000);
									} else {
										that.$queue.showToast(res.msg)
									}
								});
							}
						}
					}
				});
			},
			getDetail() {
				this.$queue.showLoading('加载中...')
				this.$Request.getT('/app/orders/selectOrdersDetails?ordersNo=' + this.id).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						this.orderInfo = res.data;

						if (res.data.payTime) {

							//当订单正在使用中的时候，计算一下倒计时
							if (res.data.status == 3) {
								this.timeElapsedSince(res.data.payTime)
								clearInterval(this.nowInter)
								this.nowInter = null
								this.nowInter = setInterval(() => {
									this.timeElapsedSince(res.data.payTime)
								}, 1000)
							}


							//订单结束的时候计算一下使用了多少时间this.timeDifference(res.data.payTime, res.data.endTime)
							if (res.data.status == 4 || res.data.status == 6) {
								this.timeDifference(res.data.payTime, res.data.endTime)
							}
						}

					}
				});
			},
			//计算使用中的订单当前使用的时间
			timeElapsedSince(startTime) {
				var now = new Date();
				var start = new Date(startTime);
				var diff = now - start;

				var hours = Math.floor(diff / (1000 * 60 * 60));
				var minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
				var seconds = Math.floor((diff % (1000 * 60)) / 1000);
				this.nowUseTime = (hours >= 10 ? hours : hours) + '时' + (minutes >= 10 ? minutes : '0' + minutes) + '分'
			},
			//计算结束的订单使用了多长时间
			timeDifference(startTime, endTime) {
				var start = new Date(startTime);
				var end = new Date(endTime);
				var diff = end - start;

				var hours = Math.floor(diff / (1000 * 60 * 60));
				var minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
				var seconds = Math.floor((diff % (1000 * 60)) / 1000);
				this.nowUseTime = (hours >= 10 ? hours : hours) + '时' + (minutes >= 10 ? minutes : '0' + minutes) + '分'
			},
			goPay(ordersNo) {
				let that = this;
				this.$Request.postT('/app/wxPay/payOrders?classify=3&ordersNo=' + ordersNo).then(res => {
					if (res.code == 0) {
						uni.requestPayment({
							provider: 'wxpay',
							timeStamp: res.data.timestamp,
							nonceStr: res.data.noncestr,
							package: res.data.package,
							signType: res.data.signType,
							paySign: res.data.sign,
							success: function(suc) {
								uni.hideLoading()
								that.$queue.showToast('租赁成功，等待开柜！')
								setTimeout(d => {
									that.getDetail();
								}, 2000);
							},
							fail: function(err) {
								uni.hideLoading()
								uni.showToast({
									title: '支付失败',
									icon: 'none'
								})
							}
						});
					} else {
						that.$queue.showToast(res.msg);
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #1F2A25;
	}

	.bg_view {
		background: linear-gradient(to bottom, #33513e 0%, #295640 20%, #1F2A25 60%);
		padding-top: 20rpx;
	}

	.orderStatus {
		width: 100%;
		height: auto;

		.orderStatus-box {
			width: 686rpx;
			height: 100%;
			padding: 44rpx;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border: 1px solid #92FDCB;
			border-radius: 24rpx;
			font-size: 40rpx;
			font-weight: bold;
		}
	}

	.orderTime {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.orderTime-box {
			width: 686rpx;
			height: 100%;
			padding: 44rpx;
			border-radius: 24rpx;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border: 1px solid #92FDCB;
		}

		.orderTime-box-item {
			width: 50%;
			text-align: center;

			.orderTime-box-item-t {
				font-size: 24rpx;

				text {
					font-size: 42rpx;
					font-weight: bold;
				}
			}
		}

		.orderTime-box-item-b {
			font-size: 26rpx;
			margin-top: 10rpx;
		}
	}

	.orderInfo {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.orderInfo-box {
			width: 686rpx;
			height: 100%;
			padding: 30rpx;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border: 1px solid #92FDCB;
			border-radius: 24rpx;
		}

		.orderInfo-box-title {
			font-size: 32rpx;
			font-weight: bold;
		}

		.orderInfo-box-item {
			font-weight: 500;
			margin-top: 36rpx;

			text {
				color: #CCCCCC;
			}
		}
	}

	.orderBtn {
		width: 100%;
		height: auto;
		margin-top: 50rpx;

		.orderBtn-box {
			width: 686rpx;
			height: 98rpx;
			background: linear-gradient(90deg, #7FFFDE 0%, #FDF361 100%);
			color: #2A2A2A;
			border-radius: 24rpx;
			font-size: 32rpx;
			font-weight: bold;
		}

		.orderBtn-boxs {
			width: 330rpx;
			height: 98rpx;
			background: linear-gradient(90deg, #7FFFDE 0%, #FDF361 100%);
			color: #2A2A2A;
			border-radius: 24rpx;
			font-size: 32rpx;
			font-weight: bold;
		}
	}
</style>