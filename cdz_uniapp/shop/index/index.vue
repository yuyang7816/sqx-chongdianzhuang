<template>
	<view style="padding-bottom: 40rpx;">
		<!-- 	<image src="https://qipaishi.xianmaxiong.com/file/uploadPath/2024/03/04/16720c48c04d5e9be5efff63fdf424e5.png"
			class="shop_bgimg"></image> -->
		<view>
			<view style="background: linear-gradient(to bottom,#ffffff,#f5f5f5);">
				<view class="flex align-center justify-between padding">
					<view class="flex align-center">
						<image :src="shopInfo.tearoomTitleImage?shopInfo.tearoomTitleImage:'/static/logo.png'"
							class="shop_image"></image>
						<view @click="selectShop()" class="shopname_view flex align-center">
							{{shopInfo.tearoomName}}
							<u-icon name="arrow-right" color="#333333;" size="16"></u-icon>
						</view>
					</view>
					<view v-if="shopInfo.shopType===2" class="tixianBtn" @click="goNav('/shop/wallet/index')">
						提现
					</view>

				</view>
				<view class="flex align-center justify-center" style="width: 100%;">
					<view style="width: 686rpx;background-color: #32BE62;padding: 40rpx 20rpx;border-radius: 24rpx;"
						class="flex align-center justify-around">
						<view class="shopNum_view">
							<view class="shopNum_num">{{formatMoney(sumMoney)}}</view>
							<view class="shopNum_name">总收益</view>
						</view>
						<view class="shopNum_view">
							<view class="shopNum_num">{{formatMoney(shopMoney)}}</view>
							<view class="shopNum_name">已结算收益</view>
						</view>
						<!-- <view class="shopNum_view">
							<view class="shopNum_num">{{formatMoney(sumShopMoney)}}</view>
							<view class="shopNum_name">已结算收益</view>
						</view> -->
					</view>
				</view>

			</view>


			<view class="shopbj_view">
				<view class="flex align-center justify-between">
					<view class="shopbj_title">订单管理</view>
					<view class="flex align-center" @tap="goOrder(0)">
						<view class="shopbj_more">查看全部</view>
						<u-icon name="arrow-right" color="#333333" size="14"></u-icon>
					</view>
				</view>
				<view class="margin-top flex justify-between text-center">
					<view @tap="goOrder(1)">
						<view class="shopbj_num">{{dzfCount}}</view>
						<view class="shopbj_name">待支付</view>
					</view>
					<view @tap="goOrder(2)">
						<view class="shopbj_num">{{dsyCount}}</view>
						<view class="shopbj_name">待使用</view>
					</view>
					<view @tap="goOrder(3)">
						<view class="shopbj_num">{{syzCount}}</view>
						<view class="shopbj_name">使用中</view>
					</view>
					<view @tap="goOrder(4)">
						<view class="shopbj_num">{{ywcCount}}</view>
						<view class="shopbj_name">已完成</view>
					</view>
				</view>
			</view>
			<view class="shopcs_view">
				<view class="flex justify-between">
					<view class="shopcs_title">到账总收益</view>
					<view class="flex justify-between">
						<view @tap="sjSelSave(1)" :class="sjSel == 1 ? 'shopcssjsel_view' : 'shopcssj_view'">日</view>
						<view @tap="sjSelSave(4)" :class="sjSel == 4 ? 'shopcssjsel_view' : 'shopcssj_view'">周</view>
						<view @tap="sjSelSave(2)" :class="sjSel == 2 ? 'shopcssjsel_view' : 'shopcssj_view'">月</view>
						<view @tap="sjSelSave(3)" :class="sjSel == 3 ? 'shopcssjsel_view' : 'shopcssj_view'">年</view>
					</view>
				</view>
				<view class="margin-top flex justify-between align-end">
					<view class="shopcs_num">{{formatMoney(zongMoney)}}</view>
					<view class="flex align-center" @click="selectTime()">
						<view class="shopcs_time">{{selTime}}</view>
						<!-- <image src="../static/dowm.png" class="shopcs_down"></image> -->
						<u-icon name="arrow-down" color="#cccccc" size="18"></u-icon>
					</view>
				</view>
			</view>
			<view class="flex justify-between margin-top-sm margin-lr">
				<view class="shopbx_item" @tap="goBxHome">
					<view class="bxitem_name">充电位管理</view>
					<view class="flex align-end">
						<view class="bxitem_text">总充电位数</view>
						<view class="bxitem_num">{{goodsCount}}</view>
					</view>
					<view class="bxitem_more flex align-center justify-center">
						<u-icon name="arrow-right" color="#ffffff" size="14"></u-icon>
					</view>
				</view>
				<view class="shopbx_item" @tap="goBxNum">
					<view class="bxitem_name">充电位数据</view>
					<view class="flex align-end">
						<view class="bxitem_text">今日收益</view>
						<view class="bxitem_num">{{nowMoney}}</view>
					</view>
					<view class="bxitem_more flex align-center justify-center">
						<u-icon name="arrow-right" color="#ffffff" size="14"></u-icon>
					</view>
				</view>
			</view>
			<view class="shopcs_view" @tap="goTimeLength()">
				<view class="flex justify-between align-center">
					<view class="shopcs_title">时长统计</view>
					<u-icon name="play-right-fill" color="#333333" size="14"></u-icon>
				</view>
			</view>
			<view class="shopcs_view" @tap="goNav('/shop/giveComp/giveComp')">
				<view class="flex justify-between align-center">
					<view class="shopcs_title">赠送优惠券</view>
					<u-icon name="play-right-fill" color="#333333" size="14"></u-icon>
				</view>
			</view>
			<view class="shopcs_view" @tap="goNav('/shop/statistics/index')">
				<view class="flex justify-between align-center">
					<view class="shopcs_title">我的门店</view>
					<u-icon name="play-right-fill" color="#333333" size="14"></u-icon>
				</view>
			</view>
			<view class="shopcs_view" @tap="goNav('/shop/statistics/incomeList')">
				<view class="flex justify-between align-center">
					<view class="shopcs_title">收入来源详情</view>
					<u-icon name="play-right-fill" color="#333333" size="14"></u-icon>
				</view>
			</view>

		</view>
		<!-- 棋牌室选择器 -->
		<u-picker :show="shopShow" @confirm="confirmShop" :columns="[shopList]" keyName="tearoomName"></u-picker>
		<!-- 选择日期 -->
		<uv-datetime-picker ref="datetimePicker" v-model="value" mode="date" @confirm="confirmTime">
		</uv-datetime-picker>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				value: Number(new Date()),
				shopShow: false,
				sjSel: 1,
				selTime: uni.$u.timeFormat(new Date(), 'yyyy-mm-dd'),
				shopList: [], //棋牌室列表
				shopInfo: {}, //当前棋牌室信息
				tearoomId: '', //当前棋牌室的id
				sumMoney: 0.00, //总收益
				dzfCount: 0, //待支付订单
				dsyCount: 0, //待使用订单
				syzCount: 0, //使用中订单
				ywcCount: 0, //已完成订单
				goodsCount: 0, //包厢数量
				nowMoney: 0, //今日包厢收益
				sumShopMoney: 0, // 已结算金额
				shopMoney: 0, //待结算金额
				zongMoney: 0,
			}
		},
		onLoad() {
			this.getShopList()

		},
		methods: {
			goNav(url) {
				uni.navigateTo({
					url: url + '?tearoomId=' + this.tearoomId
				})
			},
			//去时长统计页面
			goTimeLength() {
				uni.navigateTo({
					url: '/shop/timeLength/timeLength?tearoomId=' + this.tearoomId
				})
			},
			//选择日期重新获取数据
			confirmTime(e) {
				this.selTime = uni.$u.timeFormat(e.value, 'yyyy-mm-dd')
				if (this.sjSel != 4) { //不是周
					this.getTearoomOrdersByTearoomIddAndTime(this.sjSel, this.selTime)
				} else { //周
					this.getTearoomOrdersByTearoomIddAndTime(this.sjSel, this.getWeekStartAndEnd(this.selTime).start,
						this
						.getWeekStartAndEnd(this.selTime).end)
				}
			},
			//打开选择日期弹窗
			selectTime() {
				this.$refs.datetimePicker.open();
			},
			/**
			 * @param {Object} flag 类型：1日2月3年4周
			 * @param {Object} time 时间
			 * @param {Object} endTime 结束时间
			 */
			getTearoomOrdersByTearoomIddAndTime(flag, time, endTime) {
				let data = {
					flag: flag,
					time: time,
					tearoomId: this.tearoomId,
				}
				if (flag == 4) {
					data.endTime = endTime
				}
				this.$Request.getT('/app/orders/selectTearoomOrdersByTearoomIddAndTime', data).then(res => {
					if (res.code == 0) {
						if (res.data && res.data.money && res.data.money > 0) {
							this.zongMoney = res.data.money
						} else {
							this.zongMoney = 0
						}

						// alert(res.data.money)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//获取包厢订单统计
			getOrdersStatusListByTearoomId() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/orders/selectOrdersStatusListByTearoomId', data).then(res => {
					if (res.code == 0) {
						this.dzfCount = res.data.dzfCount
						this.dsyCount = res.data.dsyCount
						this.syzCount = res.data.syzCount
						this.ywcCount = res.data.ywcCount
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//保留两位小数
			formatMoney(num) {
				// 使用 toFixed() 方法保留两位小数  
				return num.toFixed(2);
			},
			//切换棋牌室
			confirmShop(e) {
				this.sjSel = 1
				this.shopInfo = e.value[0]
				this.tearoomId = e.value[0].tearoomId
				this.getOrdersMoneyByTearoomId()
				this.getOrdersStatusListByTearoomId()
				//收益统计
				this.getTearoomOrdersByTearoomIddAndTime(1, this.selTime)
				this.shopShow = false

			},
			//查询棋牌室数据
			getOrdersMoneyByTearoomId() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/orders/selectOrdersMoneyByTearoomId', data).then(res => {
					if (res.code == 0 && res.data) {
						if (res.data.sumMoney && res.data.sumMoney > 0) {
							this.sumMoney = res.data.sumMoney //总收益
						}
						if (res.data.goodsCount && res.data.goodsCount > 0) {
							this.goodsCount = res.data.goodsCount //包厢数量
						}
						if (res.data.nowMoney && res.data.nowMoney > 0) {
							this.nowMoney = res.data.nowMoney //今日包厢收益
						}
						if (res.data.sumShopMoney && res.data.sumShopMoney > 0) {
							this.sumShopMoney = res.data.sumShopMoney //已结算金额
						}
						if (res.data.shopMoney && res.data.shopMoney > 0) {
							this.shopMoney = res.data.shopMoney //待结算金额
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//显示棋牌室弹窗
			selectShop() {
				if (this.shopList.length > 1) {
					this.shopShow = true
				} else {
					uni.showToast({
						title: '暂无更多店铺',
						icon: 'none'
					})
				}

			},
			//获取棋牌室列表
			getShopList() {
				let data = {
					userId: uni.getStorageSync('userId')
				}
				this.$Request.getT('/app/tearoom/selectTearoomList', data).then(res => {
					if (res.code == 0) {
						this.shopList = res.data
						this.shopInfo = this.shopList[0]
						this.tearoomId = this.shopList[0].tearoomId
						this.getOrdersMoneyByTearoomId()
						this.getOrdersStatusListByTearoomId()
						//收益统计
						this.getTearoomOrdersByTearoomIddAndTime(1, this.selTime)

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			/**
			 * @param {Object} date 日期
			 * 获取传入日期所在周的开始日期根结束日期
			 */
			getWeekStartAndEnd(dates) {
				//先把接收到日期转为date对象
				const date = new Date(dates)
				// 设置日期为周一（如果传入日期是周日，则设置为下周的周一）  
				const dayOfWeek = date.getDay();
				let startOfWeek = new Date(date.getTime());
				if (dayOfWeek === 0) { // 如果是周日  
					startOfWeek.setDate(startOfWeek.getDate() + 7);
				} else {
					startOfWeek.setDate(startOfWeek.getDate() - dayOfWeek + 1);
				}
				// 设置日期为周日（如果传入日期是周六，则设置为本周的周日）  
				let endOfWeek = new Date(startOfWeek.getTime());
				endOfWeek.setDate(endOfWeek.getDate() + 6);
				return {
					start: uni.$u.timeFormat(startOfWeek, 'yyyy-mm-dd'), //返回这周的开始时间
					end: uni.$u.timeFormat(endOfWeek, 'yyyy-mm-dd') //返回这周的结束时间
				};
			},
			/**
			 * @param {Object} index 类型 1:天 2:月 3:年 4:周
			 */
			sjSelSave(index) {
				this.sjSel = index;
				if (index != 4) { //不是周
					this.getTearoomOrdersByTearoomIddAndTime(index, this.selTime)
				} else { //周
					this.getTearoomOrdersByTearoomIddAndTime(index, this.getWeekStartAndEnd(this.selTime).start,
						this
						.getWeekStartAndEnd(this.selTime).end)
				}

			},
			goBj() {
				uni.navigateTo({
					url: '/my/shop/bjList?tearoomId=' + this.tearoomId
				});
			},
			goOrder(index) {
				uni.navigateTo({
					url: '/shop/bxOrder/bxOrder?index=' + index + '&tearoomId=' + this.tearoomId
				});
			},
			goBxHome() {
				uni.navigateTo({
					url: '/shop/bxHome/bxHome?tearoomId=' + this.tearoomId
				});
			},
			goBxNum() {
				uni.navigateTo({
					url: '/shop/bxNum/bxNum?tearoomId=' + this.tearoomId
				});
			}
		}
	}
</script>

<style lang="scss">
	page {
		background: #F5F5F5;
	}

	.tixianBtn {
		background-color: #32be62;
		color: #ffffff;
		padding: 10rpx 20rpx;
		border-radius: 16rpx;
	}

	.shop_bgimg {
		width: 750rpx;
		height: 500rpx;
		position: absolute;
		top: 0rpx;
		background: linear-gradient(to bottom, #FFFFFF, #F5F5F5);
	}

	.shopbx_item {
		width: 336rpx;
		height: 170rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		position: relative;

		.bxitem_more {
			width: 44rpx;
			height: 44rpx;
			position: absolute;
			right: 30rpx;
			top: 60rpx;
			background-color: #999999;
			border-radius: 50%;
		}

		.bxitem_text {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
			margin-bottom: 4rpx;
		}

		.bxitem_num {
			font-size: 42rpx;
			font-family: DINPro;
			font-weight: 500;
			color: #32BE62;
			margin-left: 10rpx;
		}

		.bxitem_name {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}
	}

	.shopcs_view {
		width: 686rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 0 auto;
		margin-top: 20rpx;
		padding: 30rpx;

		.shopcs_time {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #666666;
		}

		.shopcs_num {
			font-size: 58rpx;
			font-family: DINPro;
			font-weight: 500;
			color: #32BE62;
		}

		.shopcs_down {
			width: 15rpx;
			height: 9rpx;
			margin-left: 10rpx;

		}

		.shopcs_title {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}

		.shopcssj_view {
			width: 100rpx;
			height: 50rpx;
			text-align: center;
			line-height: 50rpx;
		}

		.shopcssjsel_view {
			width: 100rpx;
			height: 50rpx;
			line-height: 50rpx;
			background: #f4f4f4;
			border-radius: 24rpx;
			text-align: center;
		}
	}

	.shopbj_view {
		width: 686rpx;
		background: #ffffff;
		border-radius: 24rpx;
		margin: 0 auto;
		margin-top: 20rpx;
		padding: 24rpx 30rpx;

		.shopbj_num {
			font-size: 42rpx;
			font-family: DINPro;
			font-weight: 500;
			color: #333333;
		}

		.shopbj_name {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
			margin-top: 10rpx;
		}

		.shopbj_more {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
		}

		image {
			width: 10rpx;
			height: 20rpx;
			margin-left: 10rpx;
		}

		.shopbj_title {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}
	}

	.shop_image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50rpx;
	}

	.shopNum_view {
		text-align: center;
	}

	.shopNum_name {
		font-size: 24rpx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #ffffff;
	}

	.shopNum_num {
		font-size: 38rpx;
		font-family: DINPro;
		font-weight: 500;
		color: #ffffff;
		margin-top: 18rpx;
	}

	.shopname_view {
		margin-left: 20rpx;
		font-size: 32rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #333333;
	}
</style>