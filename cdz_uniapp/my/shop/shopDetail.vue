<template>
	<view>
		<view v-if="tearoomInfo.tearoomId">
			<image :src="tearoomInfo.tearoomTitleImage" class="top_image" mode="aspectFill" @tap="saveImg(tearoomInfo.tearoomTitleImage)"></image>

			<view class="top_item">
				<view class="item_name">{{tearoomInfo.tearoomName}}</view>
				<view class="item_time">营业时间：{{tearoomInfo.startTime}}-{{tearoomInfo.endTime}}</view>
				<view class="flex margin-top-sm">
					<view class="item_zj">
						可租借 {{tearoomInfo.goodsStatusCount1 ? tearoomInfo.goodsStatusCount1 : 0}}
					</view>
					<view class="item_no_zj">
						不可租借 {{tearoomInfo.goodsStatusCount2 ? tearoomInfo.goodsStatusCount2 : 0}}
					</view>
				</view>
				<view class="item_address flex align-center justify-between">
					<view class="flex align-center">
						<image src="/static/images/index/address.png" mode=""></image>
						<view style="width: 500rpx;text-overflow: ellipsis;white-space: nowrap;overflow: hidden;">
							{{tearoomInfo.tearoomProvince}}{{tearoomInfo.tearoomCity}}{{tearoomInfo.tearoomDistrict}}{{tearoomInfo.tearoomAddress}}
						</view>
					</view>
					<view class="flex align-center" @tap="openMap(tearoomInfo)">
						<view class="">导航</view>
						<u-icon name="arrow-right" color="#ffffff" size="14"></u-icon>
					</view>
				</view>

				<view class="list list-box flex justify-between flex-wrap">
					<view class="list-box-item" v-for="(item,index) in list" :key="index" @tap="goDetail(item)">
						<block v-if="tearoomInfo.status !=2">
							<image v-if="item.goodsStatus == 1"
								:src="item.goodsCoverImage?item.goodsCoverImage:'/static/logo.png'" mode=""></image>
							<image v-else :src="item.goodsTitleImage?item.goodsTitleImage:'/static/logo.png'" mode="">
							</image>
						</block>
						<block v-else>
							<image :src="item.goodsTitleImage?item.goodsTitleImage:'/static/logo.png'" mode="">
							</image>
						</block>

						<view v-if="item.goodsStatus == 1" class="list-box-item-info flex">
							<view class="list-box-item-info-title">
								{{item.goodsName}}
							</view>
							<view v-if="item.goodsType" class="list-box-item-info-goodsType">
								{{item.goodsType}}
							</view>
							<block v-if="tearoomInfo.status != 2">
								<!-- <view v-if="(isReserve == 1 || isReserve === 1) && getShows() &&isReservation"
									class="list-box-item-info-yy flex align-center justify-center">
									<view @click="setClick(item,1)"
										class="list-box-item-info-yy-l flex align-center justify-center">
										预定
									</view>
									<view @click="setClick(item,2)"
										class="list-box-item-info-yy-r flex align-center justify-center">
										{{item.goodsCategory===4?'立即租用':'立即开台'}}
									</view>
								</view>
								<view v-else class="list-box-item-info-yy flex align-center justify-center">
									<view style="border-radius: 32rpx;" @click="setClick(item,2)"
										class="list-box-item-info-yy-r flex align-center justify-center">
										{{item.goodsCategory===4?'立即租用':'立即开台'}}
									</view>
								</view> -->
								<view class="list-box-item-info-price" v-if="item.yyId">
									价值:¥{{item.yyId}}
								</view>
								<view class="list-box-item-info-price" v-if="item.hourPrice">
									计费:¥{{item.hourPrice}}(每小时)
								</view>
							</block>
							<block v-else>
								<view class="" style="color: #ffffff;margin-top: 10rpx;">
									门店已打烊
								</view>
								<view class="list-box-item-info-price">
									营业时间：{{openTime}}
								</view>
							</block>
						</view>
						<view v-else class="list-box-item-infos flex">
							<view class="list-box-item-infos-title">
								{{item.goodsName}}
							</view>
							<block v-if="item.ordersEndTime">
								<view class="list-box-item-infos-time">
									<indexListTime @timeEnd="timeEnd()" :ordersEndTime="item.ordersEndTime" />
								</view>
							</block>
							<view v-else class="list-box-item-infos-tips">
								使用中
							</view>

						</view>
					</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	import indexListTime from '@/components/indexListTime.vue'
	export default {
		components: {
			indexListTime
		},
		data() {
			return {
				list: [],
				tearoomId: '',
				tearoomInfo: {
					tearoomId: ''
				}
			}
		},
		onLoad(d) {
			if (d.tearoomName) {
				uni.setNavigationBarTitle({
					title: d.tearoomName
				})
			}

			if (d.tearoomId) {
				this.tearoomId = d.tearoomId
			}

			this.getTearoomIdList();
			this.getStorelist();
		},
		methods: {
			saveImg(image) {
				let that = this;
				let imgArr = []
				imgArr.push(image);
				//预览图片
				uni.previewImage({
					urls: imgArr,
					current: imgArr[0]
				});
			},
			goDetail(item){
				uni.navigateTo({
					url: '/my/shop/itemDetail?tearoomId=' + item.tearoomId + '&goodsId=' + item.goodsId
				});
			},
			getStorelist() {
				let data = {
					tearoomId: this.tearoomId,
					status: 1
				}
				this.$Request.getT('/app/goods/selectGoodsList', data).then(res => {
					uni.hideLoading()
					uni.stopPullDownRefresh()
					if (res.code == 0) {
						this.list = res.data
					} else {
						this.list = []
					}
				})
			},
			openMap(item) {
				let latitude = item.tearoomLatitude
				let longitude = item.tearoomLongitude
				let name = item.tearoomName
				let address = item.tearoomAddress
				uni.openLocation({
					latitude: latitude - 0, //要去的纬度-地址       
					longitude: longitude - 0, //要去的经度-地址
					name: address, //地址名称
					address: name, //详细地址名称
					success: function() {
						console.log('导航成功');
					},
					fail: function(error) {
						console.log(error)
					}
				});
			},
			getTearoomIdList() {
				this.$Request.getT('/app/tearoom/selectTearoomById?tearoomId=' + this.tearoomId).then(res => {
					if (res.code == 0) {
						this.tearoomInfo = res.data
						uni.setNavigationBarTitle({
							title: this.tearoomInfo.tearoomName
						})
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #21221D;
	}

	.top_image {
		width: 750rpx;
		height: 446rpx;
	}

	.top_item {
		width: 750rpx;
		background: #21221D;
		border-radius: 40rpx 40rpx 0px 0px;
		padding: 38rpx 40rpx;
		position: relative;
		margin-top: -40rpx;

		.item_name {
			font-family: PingFang SC;
			font-weight: 800;
			font-size: 34rpx;
			color: #FFFFFF;
		}

		.item_time {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 24rpx;
			color: #999999;
			margin-top: 20rpx;
		}

		.item_zj {
			background: #5ED769;
			border-radius: 6rpx;
			padding: 4rpx 16rpx;
			font-size: 24rpx;
		}

		.item_no_zj {
			background: #FFA53F;
			border-radius: 6rpx;
			padding: 4rpx 16rpx;
			margin-left: 20rpx;
			font-size: 24rpx;
		}

		.item_address {
			font-family: PingFang SC;
			font-weight: 500;
			font-size: 26rpx;
			color: #CCCCCC;
			margin-top: 20rpx;

			image {
				width: 29rpx;
				height: 30rpx;
				margin-right: 10rpx;
			}
		}
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.list-box {
			width: 686rpx;
			height: 100%;
		}

		.list-box-item {
			width: 326rpx;
			height: 240rpx;
			margin-top: 32rpx;
			position: relative;

			image {
				width: 100%;
				height: 100%;
			}
		}

		.list-box-item-info {
			position: absolute;
			width: 100%;
			height: 100%;
			top: 0;
			left: 0;
			flex-direction: column;
			align-items: center;
			justify-content: center;

			.list-box-item-info-title {
				color: #FFFFFF;
				font-size: 40rpx;
				font-weight: 800;
			}

			.list-box-item-info-goodsType {
				font-size: 24rpx;
				color: #FFFFFF;
			}

			.list-box-item-info-yy {
				color: #FFFFFF;
				font-weight: 600;
				margin-top: 10rpx;
			}

			.list-box-item-info-yy-l {
				width: 96rpx;
				height: 50rpx;
				border-radius: 32rpx 0 0 32rpx;
				background: rgba(255, 255, 255, 0.3);

			}

			.list-box-item-info-yy-r {
				width: 140rpx;
				height: 50rpx;
				border-radius: 0 32rpx 32rpx 0;
				background: rgba(255, 255, 255, 0.2);
			}

			.list-box-item-info-price {
				color: #FFFFFF;
				font-size: 24rpx;
				margin-top: 10rpx;
			}
		}

		.list-box-item-infos {
			width: 100%;
			height: 100%;
			background: rgba(255, 255, 255, 0.1);
			position: absolute;
			top: 0;
			left: 0;
			flex-direction: column;
			align-items: center;
			justify-content: center;

			.list-box-item-infos-title {
				color: #11DB00;
				font-size: 40rpx;
				font-weight: 800;
			}

			.list-box-item-infos-tips {
				color: #FFFFFF;

			}

			.list-box-item-infos-time {
				margin-top: 10rpx;
			}
		}
	}
</style>