<template>
	<view>
		<!-- #ifdef H5 -->
		<u-sticky bgColor="#f5f5f5" customNavHeight="0">
		<!-- #endif -->
			<!-- #ifndef H5 -->
			<u-sticky bgColor="#f5f5f5">
			<!-- #endif -->
				<view class="topTime flex align-center justify-center">
					<view @click="getOpen()" class="topTime-box flex align-center">
						{{startTime}}至{{endTime}}<u-icon name="arrow-down-fill" color="#333333" size="14"></u-icon>
					</view>
				</view>
			</u-sticky>
			<view class="lists flex align-center justify-center">
				<view class="lists-box">
					<view class="lists-box-title flex align-center justify-between">
						<view v-if="type === 1" class="lists-box-title-l">
							总销售额<text>¥{{sumMoney || 0}}</text>
						</view>
						<view v-if="type === 2" class="lists-box-title-l">
							总订单数<text>{{sumCount || 0}}</text>
						</view>

						<view class="lists-box-title-r flex align-center">
							<view @click="selectType(item.id)" :class="type == item.id?'lists-box-title-r-iActive':''"
								class="lists-box-title-r-i" v-for="(item,index) in typeList" :key="index">
								{{item.name}}
							</view>
						</view>
					</view>
					<view class="lists-box-item" v-for="(item,index) in list" :key="index">
						<view class="lists-box-item-t flex align-center justify-between">
							<view class="lists-box-item-t-l">
								{{item.date}}
							</view>
							<view v-if="type === 1" class="lists-box-item-t-r">
								日额<text>¥{{item.dayTotalMoney || 0}}</text>
							</view>
							<view v-if="type === 2" class="lists-box-item-t-r">
								订单量<text>{{item.dayTotalCount || 0}}</text>
							</view>

						</view>
						<!-- <view class="lists-box-item-line flex align-center">
						<view class="lists-box-item-line-i" :style="{width:item.wxPercent+'%',background:'#36B877'}">
						</view>
						<view class="lists-box-item-line-i" style="border-radius: 0 24rpx 24rpx 0"
							:style="{width:item.mtPercent+'%',background:'#FFC803'}">
						</view>
						<view class="lists-box-item-line-i" style="border-radius: 0 24rpx 24rpx 0"
							:style="{width:item.ksPercent+'%',background:'#e4f871'}">
						</view>
						<view class="lists-box-item-line-i" style="border-radius: 0 24rpx 24rpx 0"
							:style="{width:item.dyPercent+'%',background:'#2f62f6'}">
						</view>
						<view class="lists-box-item-line-i" style="border-radius: 0 24rpx 24rpx 0"
							:style="{width:item.zsPercent+'%',background:'#f0a669'}">
						</view>
					</view> -->
						<view class="lists-box-item-line flex align-center">
							<view class="lists-box-item-line-i" :class="item.styles[0]"
								:style="{width:item.wxPercent+'%',background:'#36B877'}">
							</view>
							<view class="lists-box-item-line-i" :class="item.styles[1]"
								:style="{width:item.mtPercent+'%',background:'#FFC803'}">
							</view>
							<view class="lists-box-item-line-i" :class="item.styles[2]"
								:style="{width:item.ksPercent+'%',background:'#e4f871'}">
							</view>
							<view class="lists-box-item-line-i" :class="item.styles[3]"
								:style="{width:item.dyPercent+'%',background:'#2f62f6'}">
							</view>
							<view class="lists-box-item-line-i" :class="item.styles[4]"
								:style="{width:item.zsPercent+'%',background:'#f0a669'}">
							</view>
							<view class="lists-box-item-line-i" :class="item.styles[5]"
								:style="{width:item.sjPercent+'%',background:'#5746eb'}">
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #36B877;"></view>
								微信
							</view>
							<view v-if="type===1" class="lists-box-item-txt">
								¥{{item.wxMoney || 0 }}
							</view>
							<view v-if="type===2" class="lists-box-item-txt">
								{{item.wxCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.wxPercent}}%
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #FFC803;"></view>
								美团
							</view>
							<view v-if="type === 1" class="lists-box-item-txt">
								¥{{item.mtMoney || 0 }}
							</view>
							<view v-if="type === 2" class="lists-box-item-txt">
								{{item.mtCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.mtPercent}}%
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #e4f871;"></view>
								快手
							</view>
							<view v-if="type === 1" class="lists-box-item-txt">
								¥{{item.ksMoney || 0 }}
							</view>
							<view v-if="type === 2" class="lists-box-item-txt">
								{{item.ksCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.ksPercent}}%
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #2f62f6;"></view>
								抖音
							</view>
							<view v-if="type === 1" class="lists-box-item-txt">
								¥{{item.dyMoney || 0 }}
							</view>
							<view v-if="type === 2" class="lists-box-item-txt">
								{{item.dyCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.dyPercent}}%
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #f0a669;"></view>
								零钱
							</view>
							<view v-if="type === 1" class="lists-box-item-txt">
								¥{{item.lqMoney || 0 }}
							</view>
							<view v-if="type === 2" class="lists-box-item-txt">
								{{item.lqCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.lqPercent}}%
							</view>
						</view>
						<view class="lists-box-item-i flex align-center justify-between">
							<view class="lists-box-item-i-l flex align-center">
								<view class="lists-box-item-i-ls" style="background-color: #5746eb;"></view>
								优惠券
							</view>
							<view v-if="type === 1" class="lists-box-item-txt">
								¥{{item.yyqMoney || 0 }}
							</view>
							<view v-if="type === 2" class="lists-box-item-txt">
								{{item.yyqCount || 0 }}
							</view>
							<view class="lists-box-item-txt2">
								{{item.yyqPercent}}%
							</view>
						</view>
					</view>
					<empty marginTop="0" v-if="list.length == 0" content="暂无数据" />
				</view>
			</view>
			<uni-calendar ref="calendar" class="uni-calendar--hook" :clear-date="true" :insert="false" :lunar="true"
				:range="true" @confirm="confirm" @close="close" />
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
				mode: 'range',
				show: false,
				startTime: '',
				endTime: '',
				type: 1, //1:金额 2:订单
				typeList: [{
					name: '金额',
					id: 1
				}, {
					name: '订单',
					id: 2
				}],
				tearoomId: '',
				page: 1,
				pages: 1,
				limit: 10,
				list: [],
				sumMoney: 0,
				minDate: '',
				sumCount: 0,
			};
		},
		onLoad(option) {
			this.minDate = new Date().getFullYear() - 1 + '-01-01'
			console.log(this.minDate)
			this.startTime = uni.$u.timeFormat(new Date().getTime(), 'yyyy-mm-dd')
			this.endTime = uni.$u.timeFormat(new Date().getTime(), 'yyyy-mm-dd')
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId
				this.selectOrdersCountByDate();
			}
		},
		onPullDownRefresh() {
			this.selectOrdersCountByDate();
		},
		methods: {
			getOpen() {
				this.$refs.calendar.open()
			},
			close() {
				console.log('弹窗关闭');
			},
			//切换日期
			confirm(e) {
				console.log(e)
				this.startTime = e.range.before
				this.endTime = e.range.after
				this.selectOrdersCountByDate();
				// this.show = false
			},
			selectType(type) {
				this.type = type
				this.selectOrdersCountByDate();
			},
			selectOrdersCountByDate() {
				let data = {
					tearoomId: this.tearoomId,
					startTime: this.startTime,
					endTime: this.endTime,
					type: this.type
				}
				uni.showLoading({
					title: '加载中...'
				})
				this.$Request.getT('/app/orders/selectOrdersCountByDate', data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						// this.list = res.data.list
						this.list = res.data.list.map(item => {
							item.styles = this.calculateStyles(item);
							return item;
						});
						if (this.type === 1) {
							this.sumMoney = res.data.sumMoney
						} else {
							this.sumCount = res.data.sumCount
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			calculateStyles(item) {
				const percents = [item.wxPercent, item.mtPercent, item.ksPercent, item.dyPercent, item.zsPercent, item
					.sjPercent
				];
				const firstIndex = this.getFirstValidIndex(item);
				const lastIndex = this.getLastValidIndex(item);

				const styles = [];
				for (let i = 0; i < 6; i++) {
					if (percents[i] <= 0) {
						styles.push('');
						continue;
					}

					if (i === firstIndex && i === lastIndex) {
						styles.push('radius-both');
					} else if (i === firstIndex) {
						styles.push('radius-left');
					} else if (i === lastIndex) {
						styles.push('radius-right');
					} else {
						styles.push('');
					}
				}
				return styles;
			},
			getFirstValidIndex(item) {
				const percents = [item.wxPercent, item.mtPercent, item.ksPercent, item.dyPercent, item.zsPercent, item
					.sjPercent
				];
				for (let i = 0; i < percents.length; i++) {
					if (percents[i] > 0) return i;
				}
				return -1;
			},
			getLastValidIndex(item) {
				const percents = [item.wxPercent, item.mtPercent, item.ksPercent, item.dyPercent, item.zsPercent, item
					.sjPercent
				];
				for (let i = percents.length - 1; i >= 0; i--) {
					if (percents[i] > 0) return i;
				}
				return -1;
			},
		}
	}
</script>

<style lang="scss" scoped>
	.topTime {
		width: 100%;
		height: auto;

		.topTime-box {
			width: 686rpx;
			height: 100%;
			padding: 20rpx 0;
			color: #333333;
		}
	}

	.lists {
		width: 100%;
		height: auto;

		.lists-box {
			width: 686rpx;
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 30rpx;
		}

		.lists-box-title {
			.lists-box-title-l {
				color: #333333;
				font-size: 30rpx;
				font-weight: 500;

				text {
					color: #d93415;
					margin-left: 10rpx;
				}
			}

			.lists-box-title-r {
				background-color: #f5f5f5;
				border-radius: 40rpx;

				.lists-box-title-r-iActive {
					background-color: #32BE62 !important;
					color: #ffffff !important;
				}

				.lists-box-title-r-i {
					color: #333333;
					padding: 12rpx 20rpx;
					background-color: #f5f5f5;
					border-radius: 40rpx;

				}
			}
		}

		.lists-box-item {
			margin-top: 20rpx;
			background-color: #f8f8f8;
			border-radius: 24rpx;
			padding: 30rpx;
		}

		.lists-box-item-t {
			.lists-box-item-t-l {
				color: #7c7c7c;
			}

			.lists-box-item-t-r {
				color: #d93415;

				text {
					margin-left: 10rpx;
				}
			}
		}

		// .lists-box-item-line {
		// 	width: 570rpx;
		// 	height: 30rpx;
		// 	margin-top: 30rpx;
		// 	border-radius: 24rpx;

		// 	.lists-box-item-line-i {
		// 		height: 30rpx;
		// 		border-radius: 24rpx 0 0 24rpx;
		// 	}
		// }
		.lists-box-item-line {
			width: 570rpx;
			height: 30rpx;
			margin-top: 30rpx;
			border-radius: 24rpx;

			.lists-box-item-line-i {
				height: 30rpx;
			}

			.radius-left {
				border-radius: 24rpx 0 0 24rpx;
			}

			.radius-right {
				border-radius: 0 24rpx 24rpx 0;
			}

			.radius-both {
				border-radius: 24rpx;
			}
		}

		.lists-box-item-i {
			margin-top: 30rpx;

			.lists-box-item-i-l {
				width: 150rpx;
			}

			.lists-box-item-i-ls {
				width: 20rpx;
				height: 20rpx;
				border-radius: 50%;
				margin-right: 20rpx;
			}

			.lists-box-item-txt2 {
				width: 110rpx;
				text-align: right;
			}
		}
	}
</style>