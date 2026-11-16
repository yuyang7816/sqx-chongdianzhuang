<template>
	<view style="padding-bottom: 40rpx;">
		<view class="topNum flex align-center justify-center">
			<view class="topNum-box">
				<view class="topNum-box-title">
					总营业额（元）
				</view>
				<view class="topNum-box-money">
					{{sumMoney || 0}}
				</view>
				<view class="topNum-box-bottom flex align-center justify-between">
					<view class="topNum-box-bottom-i">
						<view class="topNum-box-bottom-i-txt">
							今天营业额
						</view>
						<view class="topNum-box-bottom-i-num">
							{{nowMoney || 0}}
						</view>
					</view>
					<view class="topNum-box-bottom-i">
						<view class="topNum-box-bottom-i-txt">
							昨天营业额
						</view>
						<view class="topNum-box-bottom-i-num">
							{{yesterdayMoney || 0}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="moneyClassify flex align-center justify-center">
			<view class="moneyClassify-box">
				<view class="moneyClassify-box-title">
					营业额分类
				</view>
				<view class="moneyClassify-box-canvas">
					<echarts :data="echartsData" />
				</view>
				<view class="moneyClassify-box-money">
					<view class="moneyClassify-box-money-i flex align-center justify-between"
						v-for="(item,index) in echartsData" :key="index">
						<view class="flex align-center">
							<image v-if="item.src" :src="item.src" mode="aspectFill"></image>
							<view v-else :style="{backgroundColor:item.color}" class="moneyClassify-box-money-l"></view>
							<text>{{item.name}}</text>
						</view>

						<view class="moneyClassify-box-money-i-t">¥ {{item.money || 0}}</view>
					</view>


					<!-- <view class="moneyClassify-box-money-i flex align-center justify-between">
						<view class="flex align-center">
							<view class="moneyClassify-box-money-l">

							</view>
							<text>其他</text>
						</view>

						<view class="moneyClassify-box-money-i-t">¥ 810455.40</view>
					</view> -->
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import echarts from '../components/echarts/echarts'
	export default {
		components: {
			echarts
		},
		data() {
			return {
				echartsData: [],
				tearoomId: '',
				dyMoney: 0, //抖音金额
				dyPercent: 0, //抖音比例
				ksMoney: 0, //快手金额
				ksPercent: 0, //快手比例
				mtMoney: 0, //美团金额
				mtPercent: 0, //美团比例
				nowMoney: 0, //今日金额
				otherMoney: 0, //其他金额
				otherPercent: 0, //其他进度
				sumMoney: 0, // 总金额
				wxMoney: 0, //微信金额
				wxPercent: 0, //微信进度
				yesterdayMoney: 0, //昨日金额
				lqMoney: 0, //零钱进度
				lqPercent: 0, //零钱进度
				yyqMoney: 0, //优惠券开台金额
				yyqPercent: 0, //优惠券开台进度
			};
		},
		onLoad(option) {
			if (option.tearoomId) {
				this.tearoomId = option.tearoomId
				this.selectOrdersDateByTearoomId();
			}
		},
		methods: {
			//获取数据
			selectOrdersDateByTearoomId() {
				let data = {
					tearoomId: this.tearoomId
				}
				this.$Request.getT('/app/orders/selectOrdersDateByTearoomId', data).then(res => {
					if (res.code == 0) {
						this.dyMoney = res.data.dyMoney //抖音金额
						this.dyPercent = res.data.dyPercent //抖音比例
						this.ksMoney = res.data.ksMoney //快手金额
						this.ksPercent = res.data.ksPercent //快手比例
						this.mtMoney = res.data.mtMoney //美团金额
						this.mtPercent = res.data.mtPercent //美团比例
						this.nowMoney = res.data.nowMoney //今日金额
						this.otherMoney = res.data.otherMoney //其他金额
						this.otherPercent = res.data.otherPercent //其他进度
						this.sumMoney = res.data.sumMoney // 总金额
						this.wxMoney = res.data.wxMoney //微信金额
						this.wxPercent = res.data.wxPercent //微信进度
						this.yesterdayMoney = res.data.yesterdayMoney //昨日金额
						this.lqMoney = res.data.lqMoney //赠送金额
						this.lqPercent = res.data.lqPercent //赠送进度
						this.yyqMoney = res.data.yyqMoney //商家开台金额
						this.yyqPercent = res.data.yyqPercent //商家开台进度

						this.echartsData = [{
								name: '微信',
								value: this.wxPercent,
								money: this.wxMoney,
								color: '#4CAF50',
								src: '/static/images/my/weixin.png'
							},
							{
								name: '美团',
								value: this.mtPercent,
								money: this.mtMoney,
								color: '#FFC107',
								src: '/shop/static/image/statistics/mtIcon.png'
							},
							{
								name: '抖音',
								value: this.dyPercent,
								money: this.dyMoney,
								color: '#E91E63',
								src: '/shop/static/image/statistics/dyIcon.png'
							},
							{
								name: '快手',
								value: this.ksPercent,
								money: this.ksMoney,
								color: '#aedd63',
								src: '/shop/static/image/statistics/ksIcon.png'
							},
							{
								name: '零钱',
								value: this.lqPercent,
								money: this.lqMoney,
								color: '#2adf9e'
							},
							{
								name: '其他',
								value: this.otherPercent,
								money: this.otherMoney,
								color: '#FF9800'
							},
							{
								name: '优惠券',
								value: this.yyqPercent,
								money: this.yyqMoney,
								color: '#5746eb'
							}
						]
					}
				})
			},
		}
	}
</script>

<style lang="scss" scoped>
	.topNum {
		width: 100%;
		height: auto;
		margin-bottom: 30rpx;

		.topNum-box {
			width: 686rpx;
			height: 100%;
			background: linear-gradient(to bottom, #32BE62, #ffffff);
			border-radius: 24rpx;
			padding: 30rpx;
			padding-bottom: 60rpx;
		}

		.topNum-box-title {
			font-size: 30rpx;
			color: #555043;
		}

		.topNum-box-money {
			margin-top: 40rpx;
			font-size: 60rpx;
			font-weight: bold;
			color: #333333;
			text-align: center;
		}

		.topNum-box-bottom {
			margin-top: 40rpx;
		}

		.topNum-box-bottom-i {
			text-align: center;

			.topNum-box-bottom-i-txt {
				color: #555043;
				font-size: 28rpx;
			}

			.topNum-box-bottom-i-num {
				color: #333333;
				font-size: 30rpx;
				font-weight: bold;
				margin-top: 10rpx;
			}
		}
	}

	.moneyClassify {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.moneyClassify-box {
			width: 686rpx;
			height: 100%;
			padding: 30rpx;
			background-color: #ffffff;
			border-radius: 24rpx;
		}

		.moneyClassify-box-title {
			font-size: 30rpx;
			color: #333333;
			font-weight: bold;
		}

		.moneyClassify-box-canvas {
			margin-top: 30rpx;
		}

		.moneyClassify-box-money-i {
			margin-top: 30rpx;

			.moneyClassify-box-money-l {
				width: 40rpx;
				height: 40rpx;
				border-radius: 8rpx;
			}

			image {
				width: 40rpx;
				height: 40rpx;
				border-radius: 50%;
			}

			text {

				margin-left: 20rpx;
			}

			.moneyClassify-box-money-i-t {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
			}
		}
	}
</style>