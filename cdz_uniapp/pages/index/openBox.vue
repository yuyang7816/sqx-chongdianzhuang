<template>
	<view>
		<view class="bgImg flex align-center justify-center">
			<image class="bgImg-box" src="/static/images/index/openBoxImg.png" mode=""></image>
		</view>
		<view class="orderInfo flex align-center justify-center">
			<view class="orderInfo-box" v-if="tearoomInfo.tearoomId">
				<view class="orderInfo-box-item flex align-start">
					<image src="/static/images/index/maps.png" mode=""></image>
					<view class="orderInfo-box-item-r">
						<view class="orderInfo-box-item-r-title">
							{{tearoomInfo.tearoomName}}
						</view>
						<view class="orderInfo-box-item-r-txt">
							{{tearoomInfo.tearoomAddress}}
						</view>
					</view>
				</view>
				<view style="margin-top: 40rpx;" class="orderInfo-box-item flex align-start" v-if="goodsInfo.goodsId">
					<image src="/static/images/order/timeIcon.png" mode=""></image>
					<view class="orderInfo-box-item-r">
						<view class="orderInfo-box-item-r-title">
							计费规则:{{goodsInfo.hourPrice}}元/60分钟，押金{{goodsInfo.cashPledge}}元
						</view>
						<view class="orderInfo-box-item-r-txt">
							前{{fzTime}}分钟内归还免费，不足60分钟按60分钟计费，押金在租赁结束后自动退回
						</view>
					</view>
				</view>
			</view>
		</view>

		<view class="orderInfo align-center justify-center" style="margin-left: 30rpx;" v-if="couponList.length > 0">
			<view class="orderInfo-box flex align-center justify-between" @tap="showq = true">
				<view class="">优惠券</view>
				<view class="">{{couponName}}</view>
			</view>
		</view>
		<view class="orderBtn flex align-center justify-center" @tap="goSave">
			<view class="orderBtn-box flex align-center justify-center">
				立即租赁
			</view>
		</view>


		<u-popup :round="20" bgColor="#2F302B" :safeAreaInsetBottom="false" :show="showq" @close="closeXz"
			mode="bottom">
			<view class="flex align-center text-center justify-center" style="position: relative;">
				<view style="font-size: 34rpx;margin-top: 20rpx;">请选择优惠券</view>
				<view style="color: #58B530;text-decoration: underline;position: absolute; right: 20rpx;top: 30rpx;"
					@tap="closeQ">取消使用</view>
			</view>
			<scroll-view scroll-y="true" style="height: 600rpx;background: #2F302B;margin-top: 20rpx;">
				<view class="list flex align-center justify-center" v-for="(item,index) in couponList">
					<view class="list-box">
						<image src="/static/compon/yhqBgimg.png"></image>
						<view class="list-box-info flex align-center justify-between">
							<view class="list-box-info-l">
								<view class="list-box-info-l-title">
									{{item.couponName}}
								</view>
								<view class="list-box-info-l-time">
									有效期至{{item.failureTime}}
								</view>
								<view class="list-box-info-l-line"></view>
								<view class="list-box-info-l-shop flex align-center">
									<block>
										当前可用
									</block>
								</view>
							</view>
							<block>
								<block>
									<view class="" @tap="couponSave(item)">
										<view class="list-box-info-r flex align-center justify-center">
											使用
										</view>
									</view>
								</block>
							</block>
						</view>
					</view>
				</view>
			</scroll-view>
		</u-popup>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				couponId: '',
				couponName: '请选择优惠券',
				showq: false,
				couponList: [],
				fzTime: '',
				tearoomId: '',
				goodsld: '',
				tearoomInfo: {
					tearoomId: ''
				},
				goodsInfo: {
					goodsId: ''
				}
			};
		},
		onLoad(d) {
			if (d.tearoomId) {
				this.tearoomId = d.tearoomId
			}

			if (d.goodsld) {
				this.goodsld = d.goodsld
			}

			// 开台几分钟内结束不扣费	871
			this.$Request.getT('/app/common/type/871').then(res => {
				if (res.code === 0) {
					this.fzTime = res.data.value;
				}
			});

			// 押金	871
			this.$Request.getT('/app/common/type/871').then(res => {
				if (res.code === 0) {
					this.yjMoney = res.data.value;
				}
			});

			this.getDetail();
		},
		methods: {
			couponSave(item) {
				this.couponId = item.couponUserId;
				this.couponName = item.couponName
				this.showq = false
			},
			closeQ() {
				this.couponId = '';
				this.couponName = '请选择优惠券'
				this.showq = false
			},
			closeXz() {
				this.showq = false
			},
			goSave() {
				let data = {
					tearoomId: this.tearoomId,
					goodsId: this.goodsld,
					couponId: this.couponId
				}
				this.$queue.showLoading('开柜中...')
				this.$Request.postJson('/app/orders/insertOrders', data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.goPay(res.data.ordersNo);
					} else {
						this.$queue.showToast(res.msg);
						setTimeout(d => {
							uni.redirectTo({
								url: '/pages/order/order'
							});
						}, 2000);
					}
				});
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
									uni.redirectTo({
										url: '/pages/order/order'
									});
								}, 2000);
							},
							fail: function(err) {
								uni.hideLoading()
								uni.showToast({
									title: '支付失败',
									icon: 'none'
								})
								uni.redirectTo({
									url: '/pages/order/order'
								});
							}
						});
					} else {
						that.$queue.showToast(res.msg);
					}
				});
			},
			getDetail() {
				this.$Request.getT('/app/tearoom/selectTearoomById?tearoomId=' + this.tearoomId).then(res => {
					if (res.code == 0) {
						this.tearoomInfo = res.data;
					}
				});

				this.$Request.getT('/app/goods/selectGoodsById?goodsId=' + this.goodsld).then(res => {
					if (res.code == 0) {
						this.goodsInfo = res.data;
					}
				});

				let data = {
					status: 1,
					page: 0,
					size: 20,
					goodsId: this.goodsld
				}
				this.$Request.getT('/app/selfCouponUser/userList', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.status == 0 && res.data) {
						if (res.data.content && res.data.content.length > 0) {
							this.couponList = res.data.content
						}
					}
				})

			}
		}

	}
</script>

<style lang="scss">
	page {
		background: #F2F6FC;
	}

	.bgImg {
		width: 100%;
		height: auto;
		padding-top: 140rpx;

		.bgImg-box {
			width: 595rpx;
			height: 545rpx;
		}
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.list-box {
			width: 686rpx;
			height: 228rpx;
			position: relative;

			image {
				width: 100%;
				height: 100%;
			}
		}

		.list-box-info {
			position: absolute;
			width: 100%;
			height: 228rpx;
			top: 0;
			left: 0;
			padding: 0 30rpx;

			.list-box-info-l {
				width: 428rpx;

				.list-box-info-l-title {
					color: #000000;
					font-size: 30rpx;
					font-weight: bold;
				}

				.list-box-info-l-time {
					color: #666666;
					font-size: 26rpx;
					margin-top: 10rpx;
				}

				.list-box-info-l-line {
					width: 100%;
					border-bottom: 1px dashed #D7D0C8;
					margin-top: 20rpx;
				}

				.list-box-info-l-shop {
					color: #666666;
					font-size: 24rpx;
					margin-top: 20rpx;
				}
			}

			.list-box-info-r-money-t {
				color: #FF6B00;
				font-size: 54rpx;

				text {
					font-size: 34rpx;
				}
			}

			.list-box-info-r-money-b {
				color: #666666;
				font-size: 24rpx;
				text-decoration-line: line-through;
			}

			.list-box-info-r {
				background-color: #58B530;
				color: #ffffff;
				font-size: 24rpx;
				width: 128rpx;
				height: 54rpx;
				border-radius: 27rpx;
			}
		}
	}

	.orderInfo {
		width: 100%;
		height: auto;
		margin-top: 46rpx;

		.orderInfo-box {
			width: 686rpx;
			height: 100%;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border: 1px solid #92FDCB;
			border-radius: 24rpx;
			padding: 40rpx;
		}

		.orderInfo-box-item {
			image {
				width: 40rpx;
				height: 40rpx;
				margin-right: 20rpx;
			}

			.orderInfo-box-item-r {
				width: calc(100% - 60rpx);

				.orderInfo-box-item-r-title {
					font-size: 30rpx;
					font-weight: bold;
				}

				.orderInfo-box-item-r-txt {
					color: #999999;
					font-size: 26rpx;
					margin-top: 10rpx;
				}
			}
		}
	}

	.orderBtn {
		width: 100%;
		height: auto;
		margin-top: 40rpx;

		.orderBtn-box {
			width: 686rpx;
			height: 98rpx;
			background: linear-gradient(90deg, #7FFFDE 0%, #FDF361 100%);
			color: #2A2A2A;
			border-radius: 24rpx;
			font-size: 32rpx;
			font-weight: bold;

		}
	}
</style>