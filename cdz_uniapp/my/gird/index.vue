<template>
	<view class="">
		<!-- 建议放在外层 -->
		<u-sticky>
			<view class="orderBox">
				<view class="flex">
					<view class="typeItem" :class="typeIndex==index?'typeItemAct':''" v-for="(item,index) in tabIte"
						:key="index">
						{{item.name}}
					</view>
				</view>
			</view>
		</u-sticky>

		<view class="" v-if="typeIndex==0">
			<view class="carbox" v-for="(item,index) in list" :key="index" @click="goDetail(item)">
				<view class="carboxInfo">
					<view class="carboxInfo_title">{{item.tearoomName}}</view>
					<view class="carboxInfo_add">{{item.tearoomAddress}}</view>
					<view class="carboxInfo_tip">
						<view class="carboxInfo_tip_kxcz">空闲{{item.manCount2}}个插座</view>
						<text></text>
						<view class="carboxInfo_tip_cz">共{{item.manCount}}个插座</view>
					</view>
				</view>
				<view class="carbox_km">
					<image src="/static/images/index/lcIcon.png"></image>
					<view>{{item.distance}}</view>
				</view>
			</view>

			<u-loadmore v-if="list.length>0" :status="status" />
			<empty v-if="list.length == 0" />
		</view>

		<view class="" v-if="typeIndex==1">
			<view class="carsbox" v-for="(item,index) in 8" :key="index" @click="goDetail">
				<view class="carsboxTop">
					<view class="carsboxTop_name">星耀台球</view>
					<view class="carsboxTop_add">
						<image src="/static/images/index/add.png"></image>
						<text>333m</text>
					</view>
				</view>
				<view class="carsboxTit">
					对外开放
					<text></text>
					24小时营业
				</view>

				<view class="carsboxLabel">
					<image src="/static/images/index/tc.png"></image>
					按实际场地收费标准收费
				</view>
				<view class="carsboxLine"></view>
				<view class="carsbox_bot">
					<view class="carsbox_botPrice">
						￥<text>1.2</text>/度
					</view>

					<view class="carsbox_botSku">
						<view class="carsbox_botSku_kuai">
							<view class="carsbox_botSku_kuai_tit">快</view>
							<view class="carsbox_botSku_kuai_num">
								闲6/<text>5</text>
							</view>
						</view>
						<view class="carsbox_botSku_man margin-left-xs">
							<view class="carsbox_botSku_man_tit">慢</view>
							<view class="carsbox_botSku_man_num">
								闲6/<text>5</text>
							</view>
						</view>
					</view>
				</view>
			</view>

			<u-loadmore :status="status" />
		</view>
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
				status: 'loadmore',
				page: 1,
				pages: 1,
				limit: 10,
				list: [],

			}
		},
		onLoad() {
			this.getList()
		},
		onShow() {

		},
		methods: {
			// 跳转详情
			goDetail(ite) {
				uni.navigateTo({
					url: '/pages/index/detail?current=' + this.typeIndex + '&tearoomId=' + ite.tearoomId
				})
			},
			// 获取列表
			getList() {
				let tearoomType = this.tabIte[this.typeIndex].id // 1.电瓶车 2.汽车桩
				let data = {
					status: 1,
					page: this.page,
					limit: 10,
					latitude: uni.getStorageSync('latitude'),
					longitude: uni.getStorageSync('longitude'),
					tearoomType: tearoomType, //充电类型 1电瓶车 2汽车桩
					city: uni.getStorageSync('city')
				}
				this.$Request.getT('/app/tearoom/selectTearoomByOrdersUserId', data).then(res => {
					uni.hideLoading();
					uni.stopPullDownRefresh();
					if (res.code == 0) {
						res.data.list.forEach((d, index) => {
							// 检查数据有效性
							if (!d) return
							if (d.distance > 1000) {
								d.distance = (d.distance / 1000).toFixed(1) + 'km'
							} else {
								d.distance = (d.distance).toFixed(1) + 'm'
							}

						});
						this.pages = res.data.totalPage
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


	}

	.carbox {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;


		.carboxInfo {
			flex: 1;

			.carboxInfo_title {
				font-family: Microsoft YaHei;
				font-weight: bold;
				font-size: 32rpx;
				color: #232323;
			}

			.carboxInfo_add {
				font-weight: 400;
				font-size: 26rpx;
				color: #232323;
				margin: 15rpx 0;
			}

			.carboxInfo_tip {
				display: flex;
				align-items: center;

				text {
					width: 1rpx;
					height: 25rpx;
					background: #E6E6E6;
					margin: 0 10rpx;
				}

				.carboxInfo_tip_kxcz {
					font-weight: 400;
					font-size: 24rpx;
					color: #FF9345;
				}

				.carboxInfo_tip_cz {
					font-weight: 400;
					font-size: 24rpx;
					color: #272727;
				}
			}
		}

		.carbox_km {
			text-align: center;

			image {
				width: 70rpx;
				height: 70rpx;
			}

			text {
				font-family: Source Han Sans CN;
				font-weight: 400;
				font-size: 24rpx;
				color: #232323;
			}
		}
	}

	.carsbox {
		margin: 20rpx 30rpx;
		background: #FFFFFF;
		border-radius: 24rpx;
		padding: 30rpx;

		.carsboxTop {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.carsboxTop_name {
				font-family: Microsoft YaHei;
				font-weight: bold;
				font-size: 32rpx;
				color: #232323;
			}

			.carsboxTop_add {
				display: flex;
				align-items: center;
				padding: 4rpx 10rpx;
				border-radius: 55rpx;
				border: 1px solid #999999;

				image {
					width: 25rpx;
					height: 27rpx;
				}

				text {
					font-family: Source Han Sans CN;
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
					margin-left: 6rpx;
				}
			}
		}

		.carsboxTit {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			display: flex;
			align-items: center;
			margin: 15rpx 0;

			text {
				display: inline-block;
				width: 1rpx;
				height: 22rpx;
				background-color: #E6E6E6;
				margin: 0 10rpx;
			}
		}

		.carsboxLabel {
			font-weight: 400;
			font-size: 21rpx;
			color: #999999;
			background: #F5F5F5;
			border-radius: 12rpx;
			display: inline-flex;
			align-items: center;
			padding: 6rpx 30rpx 6rpx 20rpx;

			image {
				width: 24rpx;
				height: 29rpx;
				margin-right: 10rpx;
			}


		}

		.carsboxLine {
			width: 623rpx;
			height: 1rpx;
			background: #E5E5E5;
			margin: 20rpx 0;
		}

		.carsbox_bot {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.carsbox_botPrice {
				font-size: 24rpx;
				color: #FF823F;

				text {
					font-weight: bold;
					font-size: 38rpx;
				}
			}

			.carsbox_botSku {
				display: flex;

				.carsbox_botSku_kuai {
					height: 42rpx;
					background: #FFFFFF;
					border-radius: 10rpx;
					border: 1px solid #FF823F;
					display: flex;

					.carsbox_botSku_kuai_tit {
						width: 40rpx;
						height: 40rpx;
						background: #FF823F;
						border-radius: 8rpx;
						border: 1px solid #FF823F;

						font-weight: 400;
						font-size: 24rpx;
						color: #FFFFFF;
						display: flex;
						align-items: center;
						justify-content: center;
					}

					.carsbox_botSku_kuai_num {
						font-weight: 400;
						font-size: 24rpx;
						color: #FF9345;
						padding: 0 15rpx;
						letter-spacing: 2rpx;
						display: flex;
						align-items: center;
						justify-content: center;

						text {
							color: #2C2C2C;
						}
					}
				}

				.carsbox_botSku_man {
					height: 42rpx;
					background: #FFFFFF;
					border-radius: 10rpx;
					border: 1px solid #5CC477;
					display: flex;

					.carsbox_botSku_man_tit {
						width: 40rpx;
						height: 40rpx;
						background: #5CC477;
						border-radius: 8rpx;
						border: 1px solid #5CC477;

						font-weight: 400;
						font-size: 24rpx;
						color: #FFFFFF;
						display: flex;
						align-items: center;
						justify-content: center;
					}

					.carsbox_botSku_man_num {
						font-weight: 400;
						font-size: 24rpx;
						color: #5CC477;
						padding: 4rpx 15rpx;
						letter-spacing: 2rpx;
						display: flex;
						align-items: center;
						justify-content: center;


						text {
							color: #2C2C2C;
						}
					}
				}
			}
		}
	}
</style>