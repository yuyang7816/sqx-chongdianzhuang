<template>
	<view>
		<view class="timeBox flex align-center justify-center">
			<view class="timeBox-contont">
				<view class="timeBox-contont-title">
					充电总时长
				</view>
				<view class="timeBox-contont-topData flex align-center justify-between">
					<view class="timeBox-contont-topData-item flex align-center justify-center">
						<text>
							{{dayMinute}}
						</text>
						<text>
							今日时长
						</text>
					</view>
					<view class="timeBox-contont-topData-item flex align-center justify-center">
						<text>
							{{monthMinute}}
						</text>
						<text>
							本月时长
						</text>
					</view>
					<view class="timeBox-contont-topData-item flex align-center justify-center">
						<text>
							{{yearMinute}}
						</text>
						<text>
							总时长
						</text>
					</view>
				</view>
				<view class="timeBox-contont-title flex align-center justify-between" style="margin-top: 40rpx;">
					充电总时长

					<!-- showTime = true -->
					<view @click="getOpen()" class="timeBox-contont-title-r flex align-center">
						<u-icon name="calendar-fill" color="#333333" size="22"></u-icon>
						<text>
							{{startTime}}至{{endTime}}
						</text>
					</view>
				</view>
				<view class="timeBox-contont-list flex align-center justify-between" v-for="(item,index) in carTimeList"
					:key="index">
					<view class="timeBox-contont-list-i flex align-center justify-center">
						<image src="/static/logo.png" mode="widthFix"></image>
						<text style="color: #333333;font-size: 24rpx;">{{item.goodsName?item.goodsName:''}}</text>
					</view>
					<view class="timeBox-contont-list-i flex align-center justify-center">
						<text>{{convertMinutesToHM(item.ordersMinute)}}</text>
						<text>总时长</text>
					</view>
					<view class="timeBox-contont-list-i flex align-center justify-center">
						<text>¥{{item.ordersMoney}}</text>
						<text>总收益</text>
					</view>
				</view>
				<empty v-if="carTimeList.length==0" />
			</view>
		</view>
		<!-- 日期范围选择 -->
		<!-- <u-calendar round="24" color="#FDA310" :allowSameDay="true" :monthNum="50" :defaultDate="[nowDate]"
			:maxDate="nowDate" :minDate="minDate" :show="showTime" mode="range" @confirm="confirm"
			@close="showTime = false"></u-calendar> -->
		<uni-calendar ref="calendar" class="uni-calendar--hook" :clear-date="true" :insert="false" :lunar="true"
			:range="true" @confirm="confirm" @close="close" />
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
				showTime: false,
				carTimeList: [],
				tearoomId: '',
				dayMinute: 0, //今日
				monthMinute: 0, //本月
				yearMinute: 0, //总
				startTime: '',
				endTime: '',
				nowDate: '',
				minDate: '',
			};
		},
		onLoad(option) {
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId
				this.getOrdersNowSumMinute()
				this.getInitTime()
			}

		},
		onPullDownRefresh() {
			this.getOrdersNowSumMinute()
			this.getInitTime()
		},
		methods: {
			getOpen() {
				this.$refs.calendar.open()
			},
			close() {
				console.log('弹窗关闭');
			},
			confirm(e) {
				// console.log(e)
				// this.startTime = e[0]
				// this.endTime = e[e.length - 1]
				// this.getOrdersNowSumMinuteByGoodsIdAndTime()
				// this.showTime = false
				//切换日期
				console.log(e)
				this.startTime = e.range.before
				this.endTime = e.range.after
				this.getOrdersNowSumMinuteByGoodsIdAndTime()
			},
			//初始化时间
			getInitTime() {
				let startTime = new Date()
				this.nowDate = this.$u.timeFormat(startTime, 'yyyy-mm-dd')
				this.minDate = Number(this.$u.timeFormat(startTime, 'yyyy')) - 2 + '-01-01'
				console.log(this.minDate)
				this.startTime = this.$u.timeFormat(startTime, 'yyyy-mm-dd')
				this.endTime = this.$u.timeFormat(startTime, 'yyyy-mm-dd')
				this.getOrdersNowSumMinuteByGoodsIdAndTime()
			},
			getOrdersNowSumMinuteByGoodsIdAndTime() {
				let data = {
					tearoomId: this.tearoomId,
					startTime: this.startTime,
					endTime: this.endTime
				}
				this.$Request.getT('/app/orders/selectOrdersNowSumMinuteByGoodsIdAndTime', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.carTimeList = res.data
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//将分钟转换为小时和分钟
			convertMinutesToHM(minutes) {
				const hours = Math.floor(minutes / 60);
				const remainingMinutes = minutes % 60;
				const formattedHours = String(hours).padStart(2, '0');
				const formattedMinutes = String(remainingMinutes).padStart(2, '0');
				return `${formattedHours}:${formattedMinutes}`;
			},
			//店铺时长统计
			getOrdersNowSumMinute() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/orders/selectOrdersNowSumMinuteByGoodsId', data).then(res => {
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.dayMinute = this.convertMinutesToHM(res.data.dayMinute)
						this.monthMinute = this.convertMinutesToHM(res.data.monthMinute)
						this.yearMinute = this.convertMinutesToHM(res.data.yearMinute)
					}
				})
			},
		},
	}
</script>

<style lang="scss">
	.timeBox {
		width: 100%;
		height: auto;
		margin-top: 32rpx;

		.timeBox-contont {
			width: 686rpx;
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 40rpx 30rpx;
		}

		.timeBox-contont-title {
			color: #474747;
			font-size: 30rpx;
			font-weight: bold;

			.timeBox-contont-title-r {
				text {
					color: #999999;
					font-size: 26rpx;
					font-weight: normal;
					margin-left: 4rpx;
				}

			}
		}

		.timeBox-contont-topData {
			width: 100%;
			background-color: #F7F7F7;
			border-radius: 16rpx;
			margin-top: 30rpx;
			padding: 30rpx;

			.timeBox-contont-topData-item {
				flex-direction: column;

				text:nth-of-type(1) {
					color: #333333;
					font-size: 36rpx;
					font-weight: 500;
				}

				text:nth-of-type(2) {
					color: #333333;
					font-size: 24rpx;
					margin-top: 10rpx;
				}
			}
		}

		.timeBox-contont-list {
			border: 1px solid #999999;
			border-radius: 24rpx;
			margin-top: 20rpx;
			padding: 20rpx 30rpx;

			.timeBox-contont-list-i {
				flex-direction: column;

				image {
					width: 90rpx;
					// height: 76rpx;
				}

				text:nth-of-type(1) {
					color: #333333;
					font-size: 36rpx;
					font-weight: 500;
				}

				text:nth-of-type(2) {
					color: #333333;
					font-size: 24rpx;
					margin-top: 16rpx;
				}
			}
		}
	}
</style>