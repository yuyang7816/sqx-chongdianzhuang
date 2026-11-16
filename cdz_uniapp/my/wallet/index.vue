<template>
	<view>
		<view class="walletbox">
			<view class="lvbox">
				<view class="lvbox_title">总资产(元)</view>
				<view class="lvbox_price"><text>{{money}}</text> 元</view>
				<!-- <view class="lvbox_tit">累计提现:325.26元</view> -->
			</view>
			<view class="waletye">
				<view class="waletye_info">
					<view class="waletye_infoTit">总资产(元)</view>
					<view class="waletye_infoNum">{{money}}</view>
				</view>
				<view class="waletye_btn" @click="goNav('/my/wallet/recharge')">充值</view>
			</view>
		</view>

		<view class="liIte">
			<view class="liItebox" @click="goNav('/my/wallet/mymoneydetail')">
				<image src="../static/qbmx.png"></image>
				<view class="liItebox_Tle">钱包明细</view>
				<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
			</view>
			<view class="liItebox " @click="goNav('/my/wallet/chongzhi')">
				<image src="../static/czjl.png"></image>
				<view class="liItebox_Tle">充值记录</view>
				<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				money: 0
			}
		},
		onLoad() {

		},
		onShow() {
			this.getMoney()
		},
		methods: {
			goNav(url){
				uni.navigateTo({
					url
				})
			},
			getMoney() {
				this.$Request.getT('/app/userMoney/selectMyMoney').then(res => {
					if (res.code == 0) {
						this.money = res.data.money
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.walletbox {
		background: #FFFFFF;
		padding: 30rpx;

		.lvbox {
			height: 312rpx;
			background: #5CC477;
			border-radius: 16rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;

			.lvbox_title {
				font-weight: 500;
				font-size: 32rpx;
				color: #FFFFFF;
			}

			.lvbox_price {
				font-weight: 500;
				font-size: 30rpx;
				color: #FFFFFF;

				text {
					font-size: 68rpx;
				}

				margin: 20rpx 0;
			}

			.lvbox_tit {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 28rpx;
				color: #FFFFFF;
			}
		}

		.waletye {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-top: 40rpx;

			.waletye_info {

				.waletye_infoTit {
					font-family: PingFang SC;
					font-weight: 500;
					font-size: 28rpx;
					color: #333333;
				}

				.waletye_infoNum {
					font-family: DINPro;
					font-weight: 500;
					font-size: 48rpx;
					color: #333333;
				}
			}

			.waletye_btn {
				width: 160rpx;
				height: 70rpx;
				background: #5CC477;
				border-radius: 8rpx;
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 32rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}
		}
	}

	.liIte {
		background: #FFFFFF;
		margin-top: 20rpx;
		padding: 30rpx 30rpx 10rpx 30rpx;


		.liItebox {
			display: flex;
			align-items: center;
			margin-bottom: 60rpx;

			image {
				width: 52rpx;
				height: 52rpx;
			}

			.liItebox_Tle {
				font-family: PingFang SC;
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
				flex: 1;
				margin-left: 10rpx;
			}
		}

	}
</style>