<template>
	<view style="padding-bottom: 40rpx;">
		<u-sticky>
			<view class="searchs flex align-center justify-center">
				<view class="searchs-box flex align-center justify-between">
					<u-search searchIconColor="#ffffff" placeholderColor="#ffffff" color="#ffffff" bgColor="#445A4A"
						:showAction="false" placeholder="搜索门店" v-model="shopName" @search="goSearch"></u-search>
					<image src="/static/images/shopList/dwIcon.png" @tap="goBack"></image>
				</view>
			</view>
		</u-sticky>
		<view class="list flex align-center justify-center">
			<view class="list-box">
				<view class="list-box-item" v-for="(item,index) in shopList" :key="index" @tap="goDetail(item)">
					<view class="flex align-center justify-between">
						<view class="list-box-item-l flex align-center">
							<image class="list-box-item-l-img"
								:src="item.tearoomTitleImage ? item.tearoomTitleImage : '/static/logo.png'"
								mode="aspectFill"></image>
							<view class="list-box-item-l-info flex align-start justify-between">
								<view class="list-box-item-l-info-title">
									{{item.tearoomName}}
								</view>
								<view class="list-box-item-l-info-txt flex align-center margin-top-xs">
									<image src="/static/images/index/address.png" mode=""></image>
									<view style="width: 330rpx;">
										{{item.tearoomAddress}}
									</view>
								</view>
								<view class="list-box-item-l-info-txt flex align-center margin-top-xs">
									<image src="/static/images/index/person.png" mode=""></image>
									<view>
										{{item.startTime}}-{{item.endTime}}
									</view>
								</view>
							</view>
						</view>
						<view class="list-box-item-r flex align-center" @tap.stop="openMap(item)">
							<image src="/static/images/shopList/dh.png" mode=""></image>
							<text>{{item.distance}}</text>
						</view>
					</view>
					<view class="list-box-item-l-info-time flex align-center margin-top-sm">
						<view class="item_item">
							可租借 {{item.goodsStatusCount1 ? item.goodsStatusCount1 : 0}}
						</view>
						<view class="item_no_item">
							不可租借 {{item.goodsStatusCount2 ? item.goodsStatusCount2 : 0}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty content="暂无门店" v-if="shopList.length == 0" />
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
				shopName: '',
				shopList: [],
				page: 1,
				limit: 10
			};
		},
		onLoad() {
			this.latitude = uni.getStorageSync('latitude')
			this.longitude = uni.getStorageSync('longitude')
			this.getList();
		},
		methods: {
			goDetail(item) {
				uni.navigateTo({
					url: '/my/shop/shopDetail?tearoomId=' + item.tearoomId + '&tearoomName=' + item.tearoomName
				});
			},
			// 一键导航
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
			goBack() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			goSearch() {
				this.page = 1;
				this.getList();
			},
			getList() {
				this.$Request.getT('/app/tearoom/selectTearoomList?sort=4&page=' + this.page + '&limit=' + this.limit +
					'&latitude=' + this.latitude + '&longitude=' + this.longitude + '&search=' + this.shopName).then(
					res => {
						if (res.code == 0) {
							if (this.page == 1) {
								this.shopList = []
							}
							res.data.list.forEach(d => {
								if (d.distance > 1000) {
									d.distance = (d.distance / 1000).toFixed(1) + 'km'
								} else {
									d.distance = (d.distance).toFixed(1) + 'm'
								}

								this.shopList.push(d);
							});
						}
						uni.hideLoading();
						uni.stopPullDownRefresh();
					});
			}
		},
		onPullDownRefresh() {
			this.page = 1;
			this.getList();
		},
		onReachBottom() {
			this.page = this.page + 1
			this.getList()
		}
	}
</script>

<style lang="scss">
	page {
		background: #1F2A25;
	}

	.searchs {
		width: 100%;
		height: auto;
		background-color: #1F2A25;

		.searchs-box {
			width: 686rpx;
			height: 100%;
			padding: 14rpx 0;

			image {
				width: 70rpx;
				height: 70rpx;
				margin-left: 20rpx;
			}
		}
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.list-box {
			width: 686rpx;
			height: 100%;
		}

		.list-box-item {
			width: 100%;
			padding: 30rpx;
			margin-bottom: 20rpx;
			border: 4rpx solid #7FFFDE;
			background: linear-gradient(244deg, #29362C 0%, #244A38 100%);
			border-radius: 35rpx;
			box-shadow: 0px 3px 15px 0px rgba(0, 0, 0, 0.05);
		}

		.list-box-item-l-info-time {
			font-family: Source Han Sans CN;
			font-weight: 400;
			font-size: 24rpx;
			color: #FFFFFF;
			margin-left: 170rpx;

			.item_item {
				background: #5ED769;
				border-radius: 6rpx;
				padding: 6rpx 16rpx;
			}

			.item_no_item {
				background: #FFA53F;
				border-radius: 6rpx;
				padding: 6rpx 16rpx;
				margin-left: 20rpx;
			}
		}

		.list-box-item-l {
			width: calc(100% - 100rpx);

			.list-box-item-l-img {
				width: 150rpx;
				height: 150rpx;
				border-radius: 20rpx;
				margin-right: 20rpx;
			}

			.list-box-item-l-info {
				padding: 10rpx 0;
				flex-direction: column;
				width: calc(100% - 170rpx);
				// height: 150rpx;

				.list-box-item-l-info-title {
					font-size: 32rpx;
					font-weight: bold;
				}

				.list-box-item-l-info-txt {
					font-size: 24rpx;
					color: #FFFFFF;

					image {
						width: 26rpx;
						height: 26rpx;
						margin-right: 10rpx;
					}
				}
			}
		}

		.list-box-item-r {
			flex-direction: column;
			width: 150rpx;


			image {
				width: 70rpx;
				height: 70rpx;
			}

			text {
				color: #FFFFFF;
				font-size: 24rpx;
				margin-top: 5rpx;
			}
		}
	}
</style>