<template>
	<view>
		<view class="headtop">
			<u-tabs :list="list" keyName="goodsName" :scrollable="true" @click="click" :activeStyle="activeStyle"
				:inactiveStyle="inactiveStyle" lineColor="#333333"></u-tabs>
		</view>

		<view class="bxNum_item">
			<view class="flex justify-between">
				<view class="bxNum_title">日数据</view>
				<view class="flex align-center" @tap="selectDate(1)">
					<view class="bxNum_time">{{dataTimes}}</view>
					<u-icon name="arrow-down" color="#cccccc" size="18"></u-icon>
				</view>
			</view>

			<view class="margin-top-sm flex justify-between text-center padding-lr-sm">
				<view>
					<view class="bxNum_num">{{riMoneyNum}}</view>
					<view class="bxNum_name">总收益</view>
				</view>
				<view>
					<view class="bxNum_num">{{riOrderNum}}</view>
					<view class="bxNum_name">订单数</view>
				</view>
			</view>
		</view>

		<view class="bxNum_item">
			<view class="flex justify-between">
				<view class="bxNum_title">周数据</view>
				<view class="flex align-center" @tap="selectDate(4)">
					<view class="bxNum_time">{{dataTimes2}}</view>
					<u-icon name="arrow-down" color="#cccccc" size="18"></u-icon>
				</view>
			</view>

			<view class="margin-top-sm flex justify-between text-center padding-lr-sm">
				<view>
					<view class="bxNum_num">{{zhouMoneyNum}}</view>
					<view class="bxNum_name">总收益</view>
				</view>
				<view>
					<view class="bxNum_num">{{zhouOrderNum}}</view>
					<view class="bxNum_name">订单数</view>
				</view>
			</view>
		</view>

		<view class="bxNum_item">
			<view class="flex justify-between">
				<view class="bxNum_title">月数据</view>
				<view class="flex align-center" @tap="selectDate(2)">
					<view class="bxNum_time">{{yearMonth}}</view>
					<u-icon name="arrow-down" color="#cccccc" size="18"></u-icon>
				</view>
			</view>

			<view class="margin-top-sm flex justify-between text-center padding-lr-sm">
				<view>
					<view class="bxNum_num">{{yueMoneyNum}}</view>
					<view class="bxNum_name">总收益</view>
				</view>
				<view>
					<view class="bxNum_num">{{yueOrderNum}}</view>
					<view class="bxNum_name">订单数</view>
				</view>
			</view>
		</view>

		<view class="bxNum_item">
			<view class="flex justify-between">
				<view class="bxNum_title">年数据</view>
				<view class="flex align-center" @tap="selectDate(3)">
					<view class="bxNum_time">{{years}}</view>
					<u-icon name="arrow-down" color="#cccccc" size="18"></u-icon>
				</view>
			</view>

			<view class="margin-top-sm flex justify-between text-center padding-lr-sm">
				<view>
					<view class="bxNum_num">{{nianMoneyNum}}</view>
					<view class="bxNum_name">总收益</view>
				</view>
				<view>
					<view class="bxNum_num">{{nianOrderNum}}</view>
					<view class="bxNum_name">订单数</view>
				</view>
			</view>
		</view>
		<!-- 日期选择 -->
		<uv-datetime-picker ref="datetimePicker" v-model="value" :mode="mode" @confirm="confirm">
		</uv-datetime-picker>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				current: 0, //0:日 1:周 2:月 3:年
				mode: 'date', //时间选择器类型
				value: Number(new Date()), //时间选择弃绑定的值
				rishow: true,
				columns: [],
				riMoneyNum: '0.00',
				riOrderNum: '0',
				zhouMoneyNum: '0.00',
				zhouOrderNum: '0',
				yueMoneyNum: '0.00',
				yueOrderNum: '0',
				nianMoneyNum: '0.00',
				nianOrderNum: '0',
				list: [],
				activeStyle: {
					color: '#333333'
				},
				inactiveStyle: {
					color: '#999999'
				},
				tearoomId: '', //棋牌室id
				nowYear: '', //当前日期的年
				nowMonth: '', //当前日期的月
				nowDay: '', //当前日期的天
				goodsId: '', //当前包厢id
				dataTimes: '', //当前年月日
				dataTimes2: '', //当前年月日
				yearMonth: '', //年月
				years: '', //年
			}
		},
		onLoad(option) {
			this.tearoomId = option.tearoomId
			this.getNowDate()
			this.getGoodsList()

		},
		methods: {
			confirm(item) {
				console.log(uni.$u.timeFormat(item.value, 'yyyy-mm-dd'))
				console.log(item, '111111111')
				switch (this.current) {
					case 0: //天
						// flag, time, endTime
						this.dataTimes = uni.$u.timeFormat(item.value, 'yyyy-mm-dd')
						this.getTearoomOrdersByGoodsIdAndTime(1, this.dataTimes)
						break;
					case 1: //周
						this.dataTimes2 = uni.$u.timeFormat(item.value, 'yyyy-mm-dd')
						this.getTearoomOrdersByGoodsIdAndTime(4, this.getWeekStartAndEnd(this.dataTimes2).start,
							this
							.getWeekStartAndEnd(this.dataTimes2).end)
						break;
					case 2: //月
						this.yearMonth = uni.$u.timeFormat(item.value, 'yyyy-mm')
						this.getTearoomOrdersByGoodsIdAndTime(2, this.yearMonth)
						break;
					case 3: //年
						this.years = uni.$u.timeFormat(item.value, 'yyyy')
						this.getTearoomOrdersByGoodsIdAndTime(3, this.years)
						break;
					default:
						break;
				}
			},
			//打开日期选择器
			selectDate(type) {
				switch (type) {
					case 1: //天
						this.mode = 'date'
						this.current = 0
						break;
					case 2: //月
						this.mode = 'year-month'
						this.current = 2
						break;
					case 3: //年
						this.mode = 'year'
						this.current = 3
						break;
					case 4: //周
						this.mode = 'date'
						this.current = 1
						break;
					default:
						break;
				}
				this.$refs.datetimePicker.open();
			},
			//获取当前时间
			getNowDate() {
				const date = new Date()
				this.nowYear = date.getFullYear() //获取年
				if (date.getMonth() + 1 > 9) {
					this.nowMonth = date.getMonth() + 1 //获取月
				} else {
					this.nowMonth = '0' + (date.getMonth() + 1) //获取月
				}
				if (date.getDate() > 9) {
					this.nowDay = date.getDate() //获取天
				} else {
					this.nowDay = '0' + date.getDate() //获取天
				}
				this.dataTimes = this.nowYear + '-' + this.nowMonth + '-' + this.nowDay //当前年月日
				this.dataTimes2 = this.nowYear + '-' + this.nowMonth + '-' + this.nowDay //当前年月日
				this.yearMonth = this.nowYear + '-' + this.nowMonth //年月
				this.years = this.nowYear //年
			},
			//保留两位小数
			formatMoney(num) {
				// 使用 toFixed() 方法保留两位小数  
				return num.toFixed(2);
			},
			//查询包厢数据
			getTearoomOrdersByGoodsIdAndTime(flag, time, endTime) {
				let data = {
					goodsId: this.goodsId,
					flag: flag,
					time: time
				}
				if (flag == 4) { //周数据
					data.endTime = endTime
				} else if (flag == 2) {
					data.time = data.time + '-01'
				} else if (flag == 3) {
					data.time = data.time + '-01-01'
				}

				this.$Request.getT('/app/orders/selectTearoomOrdersByGoodsIdAndTime', data).then(res => {
					if (res.code == 0) {
						switch (flag) {
							case 1: //日收益
								if (res.data && res.data.money && res.data.money > 0) {
									this.riMoneyNum = this.formatMoney(res.data.money)
								} else {
									this.riMoneyNum = '0.00'
								}
								this.riOrderNum = res.data.count
								break;
							case 2: //月收益
								if (res.data && res.data.money && res.data.money > 0) {
									this.yueMoneyNum = this.formatMoney(res.data.money)
								} else {
									this.yueMoneyNum = '0.00'
								}
								this.yueOrderNum = res.data.count
								break;
							case 3: //年收益
								if (res.data && res.data.money && res.data.money > 0) {
									this.nianMoneyNum = this.formatMoney(res.data.money)
								} else {
									this.nianMoneyNum = '0.00'
								}
								this.nianOrderNum = res.data.count
								break;
							case 4: //周收益
								if (res.data && res.data.money && res.data.money > 0) {
									this.zhouMoneyNum = this.formatMoney(res.data.money)
								} else {
									this.zhouMoneyNum = '0.00'
								}
								this.zhouOrderNum = res.data.count
								break;
							default:
								break;
						}

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
			// /**
			//  * @param {Object} num 当前数量
			//  * @param {Object} allNum //总数量
			//  * 返回百分比
			//  */
			// mathNum(num,allNum){
			// 	//算出比例，并保留两位小数
			// 	let nums = (Number(num) / Number(allNum)).toFixed(2)
			// 	return nums * 100
			// },
			//获取包厢列表
			getGoodsList() {
				let data = {
					tearoomId: this.tearoomId,
					status: 1
				}
				this.$Request.getT('/app/goods/selectGoodsList', data).then(res => {
					if (res.code == 0) {
						this.list = res.data
						this.goodsId = this.list[0].goodsId //初始化goodsId
						this.getTearoomOrdersByGoodsIdAndTime(1, this.dataTimes)
						this.getTearoomOrdersByGoodsIdAndTime(2, this.yearMonth)
						this.getTearoomOrdersByGoodsIdAndTime(3, this.years)
						this.getTearoomOrdersByGoodsIdAndTime(4, this.getWeekStartAndEnd(this.dataTimes).start,
							this
							.getWeekStartAndEnd(this.dataTimes).end)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			click(item) {
				console.log(item, '1111111')
				this.goodsId = item.goodsId
				this.getNowDate()
				this.getTearoomOrdersByGoodsIdAndTime(1, this.dataTimes)
				this.getTearoomOrdersByGoodsIdAndTime(2, this.yearMonth)
				this.getTearoomOrdersByGoodsIdAndTime(3, this.years)
				this.getTearoomOrdersByGoodsIdAndTime(4, this.getWeekStartAndEnd(this.dataTimes).start,
					this
					.getWeekStartAndEnd(this.dataTimes).end)
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #F7F7F7;
	}

	.headtop {
		padding: 0rpx 20rpx;
	}

	.bxNum_item {
		width: 686rpx;
		// height: 210rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;
		margin: 0 auto;
		margin-top: 20rpx;

		.bxNum_time {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #666666;
		}

		.bxNum_num {
			font-size: 58rpx;
			font-family: DINPro;
			font-weight: 500;
			color: #333333;
		}

		.bxNum_name {
			font-size: 24rpx;
			font-family: PingFang SC;
			font-weight: 500;
			color: #333333;
			margin-top: 10rpx;
		}

		.bxNum_down {
			width: 15rpx;
			height: 9rpx;
			margin-left: 10rpx;
		}

		.bxNum_title {
			font-size: 32rpx;
			font-family: PingFang SC;
			font-weight: bold;
			color: #333333;
		}
	}
</style>