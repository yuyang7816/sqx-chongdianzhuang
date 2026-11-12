<template>
	<view class="">
		<!-- 建议放在外层 -->
		<u-sticky>
			<view class="orderBox">
				<view class="flex">
					<view class="typeItem" :class="typeIndex==index?'typeItemAct':''" v-for="(item,index) in tabIte"
						@click="bindType(index)" :key="index">
						{{item.name}}
					</view>
				</view>
				<view class="flex justify-between padding-lr padding-tb-sm">
					<view class="ordeSte" :class="orderIndex==index?'ordeSteAct':''" v-for="(item,index) in orderState"
						@click="bindState(index)" :key="index">
						{{item.name}}
						<image src="../static/orderState.png" mode="widthFix" v-if="orderIndex==index"></image>
					</view>
				</view>
			</view>
		</u-sticky>

		<view class="listbox" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
			<view class="listbox_Ste">
				<view class="listbox_SteTitle">{{item.tearoomName}}</view>

				<view class="listbox_SteTit" v-if="item.status==1">待支付</view>
				<view class="listbox_SteTit" v-if="item.status==3">使用中</view>
				<view class="listbox_SteTit" v-if="item.status==4">已完成</view>
				<view class="listbox_SteTit" v-if="item.status==5">已取消</view>


			</view>
			<view class="listboxInfo">
				<view class="listboxInfo_Ite">
					<view class="listboxInfo_IteNum">{{item.payMoney}}<text>元</text></view>
					<view class="listboxInfo_IteNumTit">消费金额</view>
				</view>
				<view class="listboxInfo_Ite">
					<view class="listboxInfo_IteNum" v-if="item.status==3">
						<u-count-down :time="item.timeDiff" format="HH:mm:ss"></u-count-down>
					</view>
					<view class="listboxInfo_IteNum" v-if="item.status==4">{{item.TimerStr}}</view>
					<view class="listboxInfo_IteNum" v-if="item.status==1||item.status==5">00:00:00</view>
					<view class="listboxInfo_IteNumTit" v-if="item.status==3">剩余充电时长 </view>
					<view class="listboxInfo_IteNumTit" v-else>充电时间 </view>

				</view>
				<view class="listboxInfo_Ite">
					<view class="listboxInfo_IteNum">{{item.goodsName}}<text>号</text></view>
					<view class="listboxInfo_IteNumTit">端口号</view>
				</view>
			</view>
			<view class="listbox_bot">
				<view class="listbox_botData">{{item.createTime}}</view>
				<view class="listbox_bot_btn">查看详情</view>
			</view>
		</view>
		<empty v-if="list.length==0"></empty>
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
				tabIte: [{
					id: 1,
					name: '电动车充电'
				}, {
					id: 2,
					name: '汽车充电'
				}],
				typeIndex: 0,
				orderState: [{
					id: '',
					name: '全部'
				}, {
					id: 1,
					name: '待支付'
				}, {
					id: 3,
					name: '使用中'
				}, {
					id: 4,
					name: '已完成'
				}, {
					id: 5,
					name: '已取消'
				}],
				orderIndex: 0,
				page: 1,
				pages: 1,
				list: [],


				timeData: {},
			}
		},
		onLoad(option) {
			if (option.current) {
				this.typeIndex = option.current
			} else {
				this.typeIndex = 0
			}
		},
		onShow() {
			this.page = 1
			this.getlist()
		},
		methods: {
			onChange(e) {
				this.timeData = e
			},
			goDetail(item) {
				uni.navigateTo({
					url: '/my/order/detail?ordersNo=' + item.ordersNo
				})
			},
			// 切换汽车 电动车类型
			bindType(index) {
				this.typeIndex = index
				this.orderIndex = 0
				this.page = 1
				this.getlist()
			},
			// 订单状态切换
			bindState(index) {
				this.orderIndex = index
				this.page = 1
				this.getlist()
			},
			getlist() {
				let type = this.tabIte[this.typeIndex].id
				let orderState = this.orderState[this.orderIndex].id
				let data = {
					status: orderState,
					page: this.page,
					limit: 10,
					ordersType: type
				}
				this.$Request.getT('/app/orders/selectOrdersList', data).then(res => {
					uni.hideLoading();
					uni.stopPullDownRefresh();
					if (res.code == 0) {

						this.pages = res.data.totalPage
						res.data.list.forEach(item => {
							// console.log(item.status)
							const endTimeStr = new Date(item.endTime.replace(' ', 'T'));
							const startTimeStr = item.status == 3 ? new Date() : new Date(item.startTime
								.replace(' ', 'T'));
							item.timeDiff = endTimeStr - startTimeStr; // 单位：毫秒
							item.TimerStr = this.formatTimeDiff(item.timeDiff)
						})
						if (this.page == 1) {
							this.list = res.data.list
						} else {
							this.list = [...this.list, ...res.data.list]
						}
						if (this.pages > this.page) {
							this.status = 'loading'
						} else {
							this.status = 'nomore'
						}
					}
				});
			},
			formatTimeDiff(ms) {
				if (ms < 0) return '无效时间';
				// 计算各个单位
				const seconds = Math.floor(ms / 1000);
				const minutes = Math.floor(seconds / 60);
				const hours = Math.floor(minutes / 60);

				// 取余得到剩余部分
				const remainingSeconds = seconds % 60;
				const remainingMinutes = minutes % 60;
				return `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`
				// return {
				// 	hours,
				// 	minutes: remainingMinutes,
				// 	seconds: remainingSeconds,

				// 	// 格式化字符串
				// 	str: `${hours.toString().padStart(2, '0')}:${remainingMinutes.toString().padStart(2, '0')}:${remainingSeconds.toString().padStart(2, '0')}`,

				// 	// 中文格式
				// 	strCN: `${hours > 0 ? `${hours}小时` : ''}${remainingMinutes > 0 ? `${remainingMinutes}分钟` : ''}${remainingSeconds}秒`
				// };
			}
		},
		onPullDownRefresh() {
			this.page = 1;
			this.getlist();
		},
		onReachBottom() {
			if (this.pages > this.page) {
				this.page = this.page + 1
				this.getlist()
			} else {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}

		}
	}
</script>

<style lang="scss">
	.orderBox {
		background: #FFFFFF;

		.typeItem {
			flex: 1;
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 28rpx;
		}

		.typeItemAct {
			background: #5CC477;
			color: #FFFFFF;
		}

		.ordeSte {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #404040;
			display: flex;
			flex-direction: column;
			align-items: center;

			image {
				width: 32rpx;
				height: 10rpx;
				margin-top: 6rpx;
			}
		}

		.ordeSteAct {
			font-weight: 800 !important;
			font-size: 28rpx !important;
		}
	}

	.listbox {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.listbox_Ste {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.listbox_SteTitle {
				font-weight: 500;
				font-size: 32rpx;
				color: #232323;
			}

			.listbox_SteTit {
				font-weight: bold;
				font-size: 26rpx;
				color: #5CC477;
			}
		}

		.listboxInfo {
			background: #F5F5F5;
			display: flex;
			border-radius: 10rpx;
			margin: 15rpx 0;
			padding: 30rpx 0;
			justify-content: space-between;

			.listboxInfo_Ite {
				padding: 0 40rpx;
				text-align: center;

				::v-deep .u-count-down__text {
					font-family: Source Han Sans CN;
					font-weight: bold;
					font-size: 32rpx;
					color: #000000;
				}

				.listboxInfo_IteNum {
					font-family: Source Han Sans CN;
					font-weight: bold;
					font-size: 32rpx;
					color: #000000;

					text {
						font-size: 24rpx;
					}
				}

				.listboxInfo_IteNumTit {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #A5A6A5;
				}
			}
		}

		.listbox_bot {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 20rpx;

			.listbox_botData {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #A5A6A5;
			}

			.listbox_bot_btn {
				width: 120rpx;
				height: 50rpx;
				background: #5CC477;
				border-radius: 8rpx;
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 24rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;

			}
		}
	}
</style>